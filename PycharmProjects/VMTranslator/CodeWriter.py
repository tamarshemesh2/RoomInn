class CodeWriter:
    def __init__(self, output_file):
        self.output_file = output_file  # Opens the output file/stream and gets ready to write into it.

    def set_file_name(self, file_name):
        """
        Informs the code writer that the translation of a new VM file is started.
        :param file_name:new VM file to translate
        :type file_name: string
        """

    def write_arithmetic(self, command):
        """
        Writes the assembly code that is the translation of the given arithmetic command.
        :param command: arithmetic command to translate
        :type command: string
        """

    def write_push_pop(self, command, segment, index):
        """
        Writes the assembly code that is the translation of the given command,
        where command is either C_PUSH or C_POP.
        :param command: command type to translate
        :type command: C_PUSH or C_POP CommandType
        :param segment: specific memory segment
        :type segment: string
        :param index: index to push to/ pop from
        :type index: int
        """

    def close(self):
        """
        Closes the output file.
        :return:
        :rtype:
        """

    # project 8 additions -----------------------------------------------------------------

    def write_init(self):
        """
        Writes the assembly code that effects the VM initialization, also called bootstrap code.
        This code must be placed at the beginning of the output file.
        """

    def write_label(self, label):
        """
        Writes the assembly code that is the translation of the label command.
        :param label: label name
        :type label: string
        """

    def write_goto(self, label):
        """
        Writes the assembly code that is the translation of the goto command.
        :param label: label name
        :type label: string
        """

    def write_if(self, label):
        """
        Writes the assembly code that is the translation of the if-goto command.
        :param label: label name
        :type label: string
        """

    def write_call(self, function_name, num_args):
        """
        Writes the assembly code that is the translation of the call command.
        :param function_name: name of function to call
        :type function_name: string
        :param num_args: function args
        :type num_args: int
        """

    def write_return(self):
        """
        Writes the assembly code that is the translation of the return command.
        """

    def write_function(self, function_name, num_locals):
        """
        Writes the assembly code that is the translation of the given function command.
        :param function_name: name of function to write
        :type function_name: string
        :param num_locals: function args
        :type num_locals: int
        """