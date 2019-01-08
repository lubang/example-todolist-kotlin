import { TodoState, TodoMutations } from './store-types'
import { ActionTree, ActionContext } from 'vuex'
import axios from 'axios'
import { MessageAnalyzer } from '@/utils/MessageAnalyzer'

export const actions: ActionTree<TodoState, TodoState> = {
  async fetchTodolist({ commit, state }, { offset, rowsPerPage }) {
    try {
      commit(TodoMutations.FETCH_TODOLIST)
      const currentOffset = offset === undefined ? state.offset : offset
      const currentRowsPerPage =
        offset === undefined ? state.rowsPerPage : rowsPerPage
      axios
        .get(
          `/api/todo-items?offset=${currentOffset}&count=${currentRowsPerPage}`,
        )
        .then((response: any) => {
          commit(TodoMutations.FETCH_TODOLIST_SUCCEED, {
            offset: currentOffset,
            rowsPerPage: currentRowsPerPage,
            totalCount: response.data.totalCount,
            results: response.data.results,
          })
        })
    } catch (error) {
      commit(TodoMutations.FETCH_TODOLIST_FAILED, error)
    }
  },
  async addTodoItem({ commit }, { message }) {
    const analyzer = new MessageAnalyzer(message)
    const extractedMessage = analyzer.extractMessage()
    const response = await axios.post('/api/todo-items', {
      message: extractedMessage,
    })

    const id = response.data.id
    const dependencies = analyzer.extractDependencies()
    for (const dependencyId of dependencies) {
      axios.post(`/api/todo-items/${id}/dependencies`, { dependencyId })
    }
  },
  async editMessage({ commit }, { id, message }) {
    const analyzer = new MessageAnalyzer(message)
    const extractedMessage = analyzer.extractMessage()
    await axios.put(`/api/todo-items/${id}`, { message: extractedMessage })

    const dependencies = analyzer.extractDependencies()
    for (const dependencyId of dependencies) {
      axios.post(`/api/todo-items/${id}/dependencies`, { dependencyId })
    }
  },
  async completeTodoItem({ commit }, { id }) {
    axios.post(`/api/todo-items/${id}/completion`)
  },
  async incompleteTodoItem({ commit }, { id }) {
    axios.delete(`/api/todo-items/${id}/completion`)
  },
}
