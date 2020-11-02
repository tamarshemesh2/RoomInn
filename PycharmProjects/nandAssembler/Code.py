def initDest():
    dest = {"M": bin(1)[2, :], "D": bin(2)[2, :], "MD": bin(3)[2, :], 'AM': bin(5)[2, :], 'A': bin(4)[2, :],
            'AD': bin(6)[2, :], 'AMD': bin(7)[2, :], '': bin(0)[2, :]}
    return dest


def initComp():
    comp = {'0': [0, "101010"], '1': [0, "111111"], '-1': [0, "111010"], 'D': [0, "001100"], 'A': [0, "110000"],
            '!D': [0, "001101"], '!A': [0, "110001"], '-D': [0, "001111"], '-A': [0, "110011"], 'D+1': [0, "011111"],
            'A+1': [0, "110111"], 'D-1': [0, "001110"], 'A-1': [0, "110010"], 'D+A': [0, "000010"],
            'A-D': [0, "000111"], 'D-A': [0, "010011"], 'D&A': [0, "000000"], 'D|A': [0, "010101"], 'M': [1, "110000"],
            '!M': [1, "110001"], '-M': [1, "110011"], 'M+1': [1, "110111"], 'M-1': [1, "110010"], 'D+M': [1, "000010"],
            'D-M': [1, "010011"], 'M-D': [1, "000111"], 'D&M': [1, "000000"],
            'D|M': [1, "010101"]}  # needs to add al the shift from the submition page
    return comp


def initJump():
    jump = {'JGT': bin(1)[2, :], 'JEQ': bin(2)[2, :], 'JGE': bin(3)[2, :], 'JLT': bin(4)[2, :], 'JNE': bin(5)[2, :],
            'JLE': bin(6)[2, :], 'JMP': bin(7)[2, :], '': bin(0)[2, :]}
    return jump


class Code:
    def _init_(self):
        self._dest = initDest()
        self._comp = initComp()
        self._jump = initJump

    def dest(self, string_dest):
        return self._dest[string_dest]

    def comp(self, string_dest):
        return self._comp[string_dest]

    def jump(self, string_dest):
        return self._jump[string_dest]
