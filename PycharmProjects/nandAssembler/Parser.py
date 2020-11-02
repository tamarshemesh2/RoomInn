import re

# statics
a_pattern = re.compile("\s*@(.)+")
c_pattern = re.compile('\s*([ADM01!]+)\s*(=\s*([ADM+-10!&|]+))?\s*(;\s*(J[A-Z]{2}))?\s*')
l_pattern = re.compile('\s*(.)\s*')


class Parser:

    def __init__(self, inputFile):
        file = open(inputFile, 'r')
        self._commands = file.readlines()
        file.close()
        self._currentCommand = 0

    def hasMoreCommands(self):
        return self._currentCommand < len(self._commands) - 1

    def advance(self):
        if self.hasMoreCommands():
            self._currentCommand += 1
            return
        return Exception("Invalid command")

    def commandType(self):
        cur = self._commands[self._currentCommand]
        if re.match(a_pattern, cur):
            return 'A_COMMAND'
        if re.match(c_pattern, cur):
            return 'C_COMMAND'
        return 'L_COMMAND'

    def symbol(self):
        cur = self._commands[self._currentCommand]
        if self.commandType() == 'A_COMMAND':
            return a_pattern.search(cur).group(1)
        if self.commandType() == 'L_COMMAND':
            return l_pattern.search(cur).group(1)
        return Exception("Invalid command")

    def dest(self):
        cur = self._commands[self._currentCommand]
        if self.commandType() == 'C_COMMAND':
            return c_pattern.search(cur).group(1)

    def comp(self):
        cur = self._commands[self._currentCommand]
        if self.commandType() == 'C_COMMAND':
            return c_pattern.search(cur).group(3)

    def jump(self):
        cur = self._commands[self._currentCommand]
        if self.commandType() == 'C_COMMAND':
            return c_pattern.search(cur).group(5)
