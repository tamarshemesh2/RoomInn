def initDest():
    dest = {"M": "001", "D": "010", "MD": "011", 'AM': "101", 'A': "100",
            'AD': "110", 'AMD': "111", '': "000"}
    return dest


def initComp():
    comp = {'0': "0101010", '1': "0111111", '-1': "0111010", 'D': "0001100", 'A': "0110000",
            '!D': "0001101", '!A': "0110001", '-D': "0001111", '-A': "0110011", 'D+1': "0011111",
            'A+1': "0110111", 'D-1': "0001110", 'A-1': "0110010", 'D+A': "0000010", 'A-D': "0000111", 'D-A': "0010011",
            'D&A': "0000000", 'D|A': "0010101", 'M': "1110000", '!M': "1110001", '-M': "1110011", 'M+1': "1110111",
            'M-1': "1110010", 'D+M': "1000010", 'D-M': "1010011", 'M-D': "1000111", 'D&M': "1000000", 'D|M': "1010101"}
    return comp


def initJump():
    jump = {'JGT': "001", 'JEQ': "010", 'JGE': "011", 'JLT': "100", 'JNE': "101",
            'JLE': "110", 'JMP': "111", '': "000"}
    return jump


def initShift():
    shift = {'D<<': "101011", 'D>>': "101001", 'A<<': "101010", 'A>>': "101000", 'M<<': "101110", 'M>>': "101100"}
    return shift


class Code:

    def __init__(self):
        self._dest = initDest()
        self._comp = initComp()
        self._jump = initJump()
        self._shift = initShift()

    def dest(self, string_dest):
        if string_dest is not None:
            return self._dest[string_dest]
        return ""

    def comp(self, string_comp):
        if string_comp is not None:
            return self._comp[string_comp]
        return ""

    def jump(self, string_jump):
        if string_jump is not None:
            return self._jump[string_jump]
        return ""

    def shift(self, string_shift):
        if string_shift is not None:
            return self._shift[string_shift]
        return ""
