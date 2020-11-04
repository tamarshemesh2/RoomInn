
# ######################################### MAGIC_NUMBERS ############################################
DEST = {"M": "001", "D": "010", "MD": "011", 'AM': "101", 'A': "100", 'AD': "110", 'AMD': "111", '': "000"}

COMP = {'0': "0101010", '1': "0111111", '-1': "0111010", 'D': "0001100", 'A': "0110000", '!D': "0001101",
        '!A': "0110001", '-D': "0001111", '-A': "0110011", 'D+1': "0011111", 'A+1': "0110111",
        'D-1': "0001110", 'A-1': "0110010", 'D+A': "0000010", 'A-D': "0000111", 'D-A': "0010011",
        'D&A': "0000000", 'D|A': "0010101", 'M': "1110000", '!M': "1110001", '-M': "1110011", 'M+1': "1110111",
        'M-1': "1110010", 'D+M': "1000010", 'D-M': "1010011", 'M-D': "1000111", 'D&M': "1000000", 'D|M': "1010101"}

JUMP = {'JGT': "001", 'JEQ': "010", 'JGE': "011", 'JLT': "100", 'JNE': "101", 'JLE': "110", 'JMP': "111", '': "000"}

SHIFT = {'D<<': "101011", 'D>>': "101001", 'A<<': "101010", 'A>>': "101000", 'M<<': "101110", 'M>>': "101100"}

EMPTY = ""
# ####################################################################################################


class Code:
    """
    Code class- manages the translation of the instructions to their binary form
    """

    def __init__(self):
        self._dest = DEST  # dest table
        self._comp = COMP  # comp table
        self._jump = JUMP  # jump table
        self._shift = SHIFT  # shift table

    def dest(self, string_dest):
        """
        returns the binary translation of the given dest instruction
        @param string_dest: dest part of an instruction
        @return: the binary form if found, empty string otherwise
        """
        if string_dest is not None:
            return self._dest[string_dest]
        return EMPTY

    def comp(self, string_comp):
        """
        returns the binary translation of the given comp instruction
        @param string_comp: comp part of an instruction
        @return: the binary form if found, empty string otherwise
        """
        if string_comp is not None:
            return self._comp[string_comp]
        return EMPTY

    def jump(self, string_jump):
        """
        returns the binary translation of the given jump instruction
        @param string_jump: jump part of an instruction
        @return: the binary form if found, empty string otherwise
        """
        if string_jump is not None:
            return self._jump[string_jump]
        return EMPTY

    def shift(self, string_shift):
        """
        returns the binary translation of the given shift instruction
        @param string_shift: shift part of an instruction
        @return: the binary form if found, empty string otherwise
        """
        if string_shift is not None:
            return self._shift[string_shift]
        return EMPTY
