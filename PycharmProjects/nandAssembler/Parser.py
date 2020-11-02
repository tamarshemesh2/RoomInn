import re

# statics
a_pattern = re.compile("\s*@(.)+")
c_pattern = re.compile('\s*([ADM01!]+)\s*(=?\s*([ADM+-10!&><|]+))?\s*(;\s*(J[A-Z]{2}))?\s*')
s_pattern = re.compile('[AMD]([<>]{2})')


class Parser:

    def __init__(self, instructions):
        self._commands = instructions
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
        if re.search(s_pattern, cur) is not None:
            return 'S_COMMAND'
        return 'C_COMMAND'

    def symbol(self):
        cur = self._commands[self._currentCommand]
        if self.commandType() == 'A_COMMAND':
            return a_pattern.search(cur).group(1)
        return Exception("Invalid command")

    def dest(self):
        cur = self._commands[self._currentCommand]
        if self.commandType() != 'A_COMMAND':
            if re.compile("=").search(cur) is not None:
                return cur.split('=')[0]
            return ""

    def comp(self):
        cur = self._commands[self._currentCommand]
        if self.commandType() != 'C_COMMAND':
            return (cur.split('=')[-1]).split(';')[0]

    def jump(self):
        cur = self._commands[self._currentCommand]
        if self.commandType() != 'A_COMMAND':
            if re.compile(";").search(cur) is not None and cur[-1] != ';':
                return cur.split(';')[-1]
            return ""

    def shift(self):
        cur = self._commands[self._currentCommand]
        if self.commandType() == 'S_COMMAND':
            return re.search(s_pattern, cur)
