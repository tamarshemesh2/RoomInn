import os
import re
import sys

from Code import Code
from Parser import Parser, CommandTypes
from SymbolTable import SymbolTable

# ######################################### MAGIC_NUMBERS ############################################
LABEL_REGEX = '\\(.+\\)'
VARIABLE_START = "@"
PARENTHESIS = '[()]'
EMPTY = ''
C_START = "111"
DIGIT_REGEX = '[\\d]+'
TO_16_BIT = "{0:016b}"
COMMENT = "//"
COMP_START = 0
COMP_REST = 1
OUTPUT_EXTENSION = ".hack"
NUM_OF_ARGS = 2
INPUT_FILE_INDEX = 1
SHIFT_FILL = "0000"
INPUT_EXTENSION = ".asm"
# ####################################################################################################


class Assembler:
    """
    Assembler class- manages the parsing and translating of the instructions
    """
    def __init__(self):
        self._symbol_table = SymbolTable()
        self._line_counter = 0

    def parse_labels(self, file_lines):
        """
        does the first pass of the file parsing, by inserting labels into the symbol table and erasing
        them from the instructions
        @param file_lines: input file lines to parse
        @return: the lines with the labels and comments removed
        """
        without_labels = []
        for line in file_lines:
            is_label = re.match(LABEL_REGEX, line)
            if bool(is_label):
                # adds label to the symbol table
                self._symbol_table.add_entry(re.sub(PARENTHESIS, EMPTY, line), self._line_counter)
                continue
            # takes line without comment if there is
            without_labels.append(line.split(COMMENT)[0])
            self._line_counter += 1
        return without_labels

    def parse_instructions(self, instructions):
        """
        does the second pass of the file parsing, by interpreting which command type is at each line,
        then translating and adding to the translated line array
        @param instructions: the file lines to parse
        @return: an array of binary translations for each instruction
        """
        parser = Parser(instructions)
        code = Code()
        translated = []
        while parser.has_more_commands():
            command_type = parser.command_type()
            # C instruction
            if command_type == CommandTypes.C:
                translate_c_instruction(parser, code, translated)
            # A instruction
            elif command_type == CommandTypes.A:
                self.translate_a_instruction(parser, translated)
            # shift command
            elif command_type == CommandTypes.Shift:
                translate_shift_command(parser, code, translated)
            parser.advance()
        return translated

    def translate_a_instruction(self, parser, translated):
        """
        translates an A instruction to binary
        @param parser: Parser object that handles the instruction parsing
        @param translated: an array of binary translations for each instruction
        """
        parsed_symbol = parser.symbol()
        is_digit = re.match(DIGIT_REGEX, parsed_symbol)
        if is_digit:
            translated.append(str(TO_16_BIT.format(int(parsed_symbol))))
        else:
            # gets existing variable address
            if self._symbol_table.contains(parsed_symbol):
                address = self._symbol_table.get_address(parsed_symbol)
                to_add = str(TO_16_BIT.format(address))
            # adds new variable to the symbol table
            else:
                self._symbol_table.add_entry(parser.get_line().split(VARIABLE_START)[1],
                                             self._symbol_table.get_counter())
                to_add = str(TO_16_BIT.format(self._symbol_table.get_counter()))
                self._symbol_table.advance_symbol_counter()
            translated.append(to_add)


def translate_c_instruction(parser, code, translated):
    """
    translates a C instruction to binary
    @param parser: Parser object that handles the instruction parsing
    @param code: Code object that handles the instruction translation
    @param translated: an array of binary translations for each instruction
    """
    comp_field = code.comp(parser.comp())
    dest_field = code.dest(parser.dest())
    jump_field = code.jump(parser.jump())
    current_translated = C_START + comp_field + dest_field + jump_field
    translated.append(current_translated)


def translate_shift_command(parser, code, translated):
    """
    translates a Shift operation to binary
    @param parser: Parser object that handles the instruction parsing
    @param code:  Code object that handles the instruction translation
    @param translated: an array of binary translations for each instruction
    """
    shift_field = parser.shift()
    dest_field = parser.dest()
    jump_field = parser.jump()
    # takes only needed fields of comp not overridden by shift
    current_translated = code.shift(shift_field) + SHIFT_FILL + code.dest(dest_field) + code.jump(jump_field)
    translated.append(current_translated)


def parse_file(assembler, filename):
    """
    handles reading input file, and calls the required functions to parse the given file
    @param assembler: Assembler object that handles managing the parsing of the file
    @param filename: the file to parse
    @return: an array of binary translations for each instruction in the file
    """
    with open(filename, 'r') as infile:
        asm_lines = []
        for line in infile:
            # skip empty lines
            no_space_line = EMPTY.join(line.split())
            if len(no_space_line) == 0 or no_space_line.startswith(COMMENT):
                continue
            else:
                asm_lines.append(no_space_line)
        without_labels = assembler.parse_labels(asm_lines)
        translated = assembler.parse_instructions(without_labels)
    infile.close()
    return translated


def create_output_file(filename, translated_lines):
    """
    handles writing the translated instructions to the output file
    @param filename: name of the output file to write to
    @param translated_lines: an array of binary translations for each instruction in the file
    """
    output_filename = filename + OUTPUT_EXTENSION
    with open(output_filename, 'w') as outfile:
        for line in translated_lines:
            outfile.write("%s\n" % line)
    outfile.close()


def check_input(file_path):
    """
    checks the given file argument, to see if it exists, is a file or directory and calls the functions to
    handle the input
    @param file_path: file argument from command line
    """
    # checks if the path given exists
    if os.path.exists(file_path):
        assembler = Assembler()
        # checks if the path leads to a file
        if os.path.isfile(file_path) and file_path.lower().endswith(INPUT_EXTENSION):
            translated = parse_file(assembler, file_path)
            dir_path = file_path.split(".")[0]
            create_output_file(dir_path, translated)
        # checks if the path leads to a directory
        elif os.path.isdir(file_path):
            files = os.listdir(file_path)
            # parse each file in the directory
            for file in files:
                if file.lower().endswith(INPUT_EXTENSION):
                    translated = parse_file(assembler, os.path.abspath(os.path.join(file_path, file)))
                    create_output_file(os.path.join(file_path, file).split(".")[0], translated)
                    assembler = Assembler()


def main():
    """
    main function of the program
    """
    if len(sys.argv) == NUM_OF_ARGS:
        file_path = sys.argv[INPUT_FILE_INDEX]
        check_input(file_path)


if __name__ == '__main__':
    main()
