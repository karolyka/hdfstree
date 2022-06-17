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
package com.trovit.hdfstree

import com.trovit.hdfstree.fsinspectors.FileSystemInspector

class TreeBuilder(private val initialPath: String, private val fileSystemInspector: FileSystemInspector) {
    fun buildTree(): TreeNode {
        return fileSystemInspector.getEntry(initialPath).also {
            buildTreeRecursively(it, initialPath, 0)
        }
    }

    private fun buildTreeRecursively(treeNode: TreeNode, currentPath: String, level: Int) {
        if (treeNode.isDirectory) {
            fileSystemInspector.list(currentPath).forEach {
                treeNode.children.add(it)
                buildTreeRecursively(it, fileSystemInspector.addSubdirToCurrent(currentPath, it.name), level + 1)
            }
        }
    }
}