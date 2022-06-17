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

abstract class FileSystemInspector(var printVerbose : Boolean = false) {

    fun verbose(message: String) {
        if (printVerbose) println(message)
    }

    abstract fun getEntry(currentPath: String): TreeNode

    /**
     * Gets the names (not total path) of the subdirectories & files for a given path.
     * @param currentPath
     */
    abstract fun list(currentPath: String): List<TreeNode>

    /**
     * Gets the string representation of a path + its subdirectory.
     * @param path Original path.
     * @param subdir subdirectory for the original path.
     * @return A valid path with the subdirectory.
     */
    abstract fun addSubdirToCurrent(path: String, subdir: String): String

}