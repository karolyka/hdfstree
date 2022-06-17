/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.trovit.hdfstree.displayers

import com.trovit.hdfstree.TreeNode

class ConsoleTreePrinter : TreePrinter {
    override var maxDepth = 100
    override var showSize = false
    private lateinit var prefix: Prefix

    override fun printTree(tree: TreeNode) {
        prefix = Prefix()

        if (showSize) {
            tree.size
        }
        printSubTree(tree, 0, true)
    }

    private fun printSubTree(node: TreeNode, level: Int, isLastChild: Boolean) {
        if (maxDepth != 0 && maxDepth > level) {
            if (node.children.size > 1) {
                prefix.markers.add(level + 1)
            }
            println(buildString {
                append(prefix.getPrefix(level, isLastChild))
                append(node.name)
                if (showSize) {
                    append(" [ ")
                    append(getHumanReadableSize(node.size))
                    append(" ]")
                }
            })

            node.folders.run {
                val lastIndex = size - 1
                forEachIndexed { index, subTree ->
                    printSubTree(subTree, level + 1, lastIndex == index)
                }
            }

        }
    }

    /**
     * Gets a nicer representation of the size of a file.
     * This is a java port of the javascript method implemented by John Strickler
     * (http://blog.jbstrickler.com/2011/02/bytes-to-a-human-readable-string/)
     * @param size
     * @return A string with the size.
     */
    private fun getHumanReadableSize(size: Long): String {
        var normalizedSize = size
        val suffix = arrayOf("bytes", "KB", "MB", "GB", "TB", "PB")
        var tier = 0
        while (normalizedSize >= 1024) {
            normalizedSize /= 1024
            tier++
        }
        return "${((normalizedSize * 10) / 10)} ${suffix[tier]}"
    }

    /**
     * Builds the prefix for each node of the tree.
     */
    inner class Prefix {
        var markers = mutableSetOf<Int>()

        fun getPrefix(level: Int, isLastChild: Boolean): String {
            return buildString {
                append(
                    (0 until level)
                        .map { i -> if (markers.contains(i)) "|" else " " }
                        .joinToString("") { "$it   " }
                )

                if (isLastChild) {
                    append("└")
                    markers.remove(level)
                } else {
                    append("├")
                }
                append("──")
            }
        }
    }
}