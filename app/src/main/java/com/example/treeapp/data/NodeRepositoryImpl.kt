package com.example.treeapp.data

class NodeRepositoryImpl(private val nodeDao: NodeDao) : NodeRepository {

    override suspend fun getChildren(id: Int): List<NodeEntity>? {
        val ans = nodeDao.getChildren(id)
        val res = mutableListOf<NodeEntity>()
        return if (ans == null) null
        else {
            ans.forEach {
                res.add(getNode(it))
            }
            res
        }
    }

    override suspend fun addNode(name: String, parent: Int?) {
        nodeDao.add(Node(0, name, parent))
    }

    override suspend fun deleteNode(id: Int) {
        nodeDao.delete(id)
    }

    override suspend fun getAll(): Int {
        return nodeDao.getAll()
    }

    private fun getNode(node: Node): NodeEntity {
        return NodeEntity(node.id, node.name, node.parent)
    }
}