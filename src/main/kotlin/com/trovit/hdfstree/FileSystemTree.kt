package com.trovit.hdfstree

import com.trovit.hdfstree.displayers.ConsoleTreePrinter
import com.trovit.hdfstree.displayers.TreePrinter
import com.trovit.hdfstree.fsinspectors.FileSystemInspector
import com.trovit.hdfstree.fsinspectors.HDFSFileSystemInspector
import com.trovit.hdfstree.fsinspectors.LocalFileSystemInspector
import org.apache.commons.cli.*
import kotlin.system.exitProcess

private const val OPTION_DEPTH = "d"
private const val OPTION_LOCAL = "l"
private const val OPTION_PATH = "p"
private const val OPTION_SIZE = "s"
private const val OPTION_VERBOSE = "v"

private const val HOME_TILDE = "~"

class FileSystemTree(args: Array<String>) {

    private val options = getOptions()
    private val commandLine: CommandLine

    private val verbose: Boolean
    private val fileSystemInspector: FileSystemInspector

    private val rootPath: String
    private val treePrinter: TreePrinter = ConsoleTreePrinter()

    init {
        try {
            commandLine = DefaultParser().parse(options, args)

            verbose = getVerbose()
            fileSystemInspector = getFileSystemInspector()
            rootPath = getRootPath()
            setMaximumDepth()
            setShowSize()

        } catch (e: ParseException) {
            println(e.message)
            HelpFormatter().printHelp("hdfs-tree", options)
            exitProcess(1)
        }
    }

    fun buildAndPrintTree() {
        TreeBuilder(rootPath, fileSystemInspector).run {
            treePrinter.printTree(buildTree())
        }
    }

    private fun setShowSize() {
        treePrinter.showSize = commandLine.hasOption(OPTION_SIZE)
    }

    private fun setMaximumDepth() {
        if (commandLine.hasOption(OPTION_DEPTH)) {
            treePrinter.maxDepth = commandLine.getOptionValue(OPTION_DEPTH).toInt()
        }
    }

    private fun getVerbose() = commandLine.hasOption(OPTION_VERBOSE)


    private fun getRootPath() = if (commandLine.hasOption(OPTION_PATH)) {
        commandLine.getOptionValue(OPTION_PATH).replaceFirst(HOME_TILDE, System.getProperty("user.home"))
    } else {
        throw ParseException("Mandatory option (-$OPTION_PATH) is not specified.")
    }

    private fun getFileSystemInspector() = if (commandLine.hasOption(OPTION_LOCAL)) {
        LocalFileSystemInspector(verbose)
    } else {
        HDFSFileSystemInspector(verbose)
    }

    private fun getOptions(): Options {
        return Options().apply {
            addOption(OPTION_LOCAL, "Use local filesystem.")
            addOption(OPTION_SIZE, "Display the size of the directory.")
            addOption(OPTION_VERBOSE, "Display the directory names while processing.")
            addOption(OPTION_PATH, true, "Path used as root for the tree.")
            addOption(OPTION_DEPTH, true, "Maximum depth of the tree (when displaying). Default")
        }
    }
}