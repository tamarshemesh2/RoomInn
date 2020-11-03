# ######################################### MAGIC_NUMBERS ############################################
DEST_TABLE = {"M": "001", "D": "010", "MD": "011", 'AM': "101", 'A': "100", 'AD': "110",
              'AMD': "111", '': "000"}

COMP_TABLE = {'0': [0, "101010"], '1': [0, "111111"], '-1': [0, "111010"], 'D': [0, "001100"],
              'A': [0, "110000"], '!D': [0, "001101"], '!A': [0, "110001"], '-D': [0, "001111"],
              '-A': [0, "110011"], 'D+1': [0, "011111"], 'A+1': [0, "110111"], 'D-1': [0, "001110"],
              'A-1': [0, "110010"], 'D+A': [0, "000010"], 'A-D': [0, "000111"], 'D-A': [0, "010011"],
              'D&A': [0, "000000"], 'D|A': [0, "010101"], 'M': [1, "110000"], '!M': [1, "110001"],
              '-M': [1, "110011"], 'M+1': [1, "110111"], 'M-1': [1, "110010"], 'D+M': [1, "000010"],
              'D-M': [1, "010011"], 'M-D': [1, "000111"], 'D&M': [1, "000000"], 'D|M': [1, "010101"]}

JUMP_TABLE = {'JGT': "001", 'JEQ': "010", 'JGE': "011", 'JLT': "100", 'JNE': "101", 'JLE': "110",
              'JMP': "111", '': "000"}

SHIFT_TABLE = {'D<<': "101011", 'D>>': "101001", 'A<<': "101010", 'A>>': "101000", 'M<<': "101110",
               'M>>': "101100"}
EMPTY = ""
# ####################################################################################################


class Code:

    def __init__(self):
        self._dest = DEST_TABLE
        self._comp = COMP_TABLE
        self._jump = JUMP_TABLE
        self._shift = SHIFT_TABLE

    def dest(self, string_dest):
        if string_dest is not None:
            return self._dest[string_dest]
        return EMPTY

    def comp(self, string_comp):
        if string_comp is not None:
            return self._comp[string_comp]
        return EMPTY

    def jump(self, string_jump):
        if string_jump is not None:
            return self._jump[string_jump]
        return EMPTY

    def shift(self, string_shift):
        if string_shift is not None:
            return self._shift[string_shift]
        return EMPTY
