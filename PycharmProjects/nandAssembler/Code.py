def initDest():
    dest = {"M": bin(1)[:3], "D": bin(2)[:3], "MD": bin(3)[:3], 'AM': bin(5)[:3], 'A': bin(4)[:3],
            'AD': bin(6)[:3], 'AMD': bin(7)[:3], '': bin(0)[:3]}
    return dest


def initComp():
    comp = {'0': [0, "101010"], '1': [0, "111111"], '-1': [0, "111010"], 'D': [0, "001100"], 'A': [0, "110000"],
            '!D': [0, "001101"], '!A': [0, "110001"], '-D': [0, "001111"], '-A': [0, "110011"], 'D+1': [0, "011111"],
            'A+1': [0, "110111"], 'D-1': [0, "001110"], 'A-1': [0, "110010"], 'D+A': [0, "000010"],
            'A-D': [0, "000111"], 'D-A': [0, "010011"], 'D&A': [0, "000000"], 'D|A': [0, "010101"], 'M': [1, "110000"],
            '!M': [1, "110001"], '-M': [1, "110011"], 'M+1': [1, "110111"], 'M-1': [1, "110010"], 'D+M': [1, "000010"],
            'D-M': [1, "010011"], 'M-D': [1, "000111"], 'D&M': [1, "000000"],
            'D|M': [1, "010101"]}
    return comp


def initJump():
    jump = {'JGT': bin(1)[:3], 'JEQ': bin(2)[:3], 'JGE': bin(3)[:3], 'JLT': bin(4)[:3], 'JNE': bin(5)[:3],
            'JLE': bin(6)[:3], 'JMP': bin(7)[:3], '': bin(0)[:3]}
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
