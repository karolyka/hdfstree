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
package com.trovit.hdfstree.fsinspectors

import com.trovit.hdfstree.TreeNode
import java.io.File

class LocalFileSystemInspector(verbose: Boolean) : FileSystemInspector(verbose) {
    override fun getEntry(currentPath: String) =
        File(currentPath).let { TreeNode(it.name, it.isDirectory, it.length()) }

    override fun list(currentPath: String): List<TreeNode> {
        verbose(currentPath)
        return File(currentPath).let { file ->
            if (file.canRead()) {
                file.listFiles()
                    ?.map { TreeNode(it.name, it.isDirectory, it.length()) }
                    ?.sortedBy { it.name }
                    ?: emptyList()
            } else {
                println("Don't have permission to read: $file. Omitting.")
                emptyList()
            }
        }
    }

    override fun addSubdirToCurrent(path: String, subdir: String) = "$path/$subdir"

}