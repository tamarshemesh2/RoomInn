def initDest():
    dest= {"M": bin(1)[2, :], "D": bin(2)[2, :], "MD": bin(3)[2, :], 'AM': bin(5)[2, :], 'A': bin(4)[2, :],
           'AD': bin(6)[2, :], 'AMD': bin(7)[2, :], '': bin(0)[2, :]}
    return dest

def initComp():
    comp={}
    comp['0'] =
    comp['1'] =
    comp['-1'] =
    comp['D'] =
    comp['A'] =
    comp['!D'] =
    comp['!A'] =
    comp['-D'] =
    comp['-A'] =
    comp['D+1'] =
    comp['A+1'] =
    comp['D-1'] =
    comp['A-1'] =
    comp['D+A'] =
    comp['A-D'] =
    comp['D-A'] =
    comp['D&A'] =
    comp['D|A'] =
    comp['M'] =
    comp['!M'] =
    comp['-M'] =
    comp['M+1'] =
    comp['M-1'] =
    comp['D+M'] =
    comp['D-M'] =
    comp['M-D'] =
    comp['D&M'] =
    comp['D|M'] =
    comp[''] =
    return comp

def initJump():
    jump=[]
    jump['JGT'] =
    jump['JEQ'] =
    jump['JGE'] =
    jump['JLT'] =
    jump['JNE'] =
    jump['JLE'] =
    jump['JMP'] =
    jump[''] =
    return jump

class Code:
    def _init_(self):
        self.dest = initDest()
        self.comp = initComp()
        self.jump = initJump

