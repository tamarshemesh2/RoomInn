import re
from enum import Enum

# ######################################### MAGIC_NUMBERS ############################################
S_PATTERN = re.compile('[AMD]([<>]{2})')
INIT_COUNTER = 0
A_COMMAND_START = '@'
# ####################################################################################################


class CommandTypes(Enum):
    C = 1
    A = 2
    Shift = 3


class Parser:

    def __init__(self, instructions):
        self._commands = instructions
        self._currentCommand = INIT_COUNTER

    def has_more_commands(self):
        return self._currentCommand < len(self._commands)

    def advance(self):
        if self.has_more_commands():
            self._currentCommand += 1
            return
        return Exception("Invalid command")

    def command_type(self):
        cur = self._commands[self._currentCommand]
        if cur[0] == A_COMMAND_START:
            return CommandTypes.A
        if re.search(S_PATTERN, cur) is not None:
            return CommandTypes.Shift
        return CommandTypes.C

    def symbol(self):
        cur = self._commands[self._currentCommand]
        if self.command_type() == CommandTypes.A:
            return cur[1:]
        return Exception("Invalid command")

    def dest(self):
        cur = self._commands[self._currentCommand]
        if self.command_type() != CommandTypes.A:
            if re.compile("=").search(cur) is not None:
                return cur.split('=')[0]
            return ""

    def comp(self):
        cur = self._commands[self._currentCommand]
        if self.command_type() == CommandTypes.C:
            return (cur.split('=')[-1]).split(';')[0]

    def jump(self):
        cur = self._commands[self._currentCommand]
        if self.command_type() != CommandTypes.A:
            if re.compile(";").search(cur) is not None and cur[-1] != ';':
                return cur.split(';')[-1]
            return ""

    def shift(self):
        cur = self._commands[self._currentCommand]
        if self.command_type() == CommandTypes.Shift:
            return re.search(s_pattern, cur)
