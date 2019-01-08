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
      await axios
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
    const dependentIds = analyzer.extractDependencies()
    await axios.post('/api/todo-items', {
      message: extractedMessage,
      dependentIds,
    })
  },
  async editMessage({ commit }, { id, message }) {
    const analyzer = new MessageAnalyzer(message)
    const extractedMessage = analyzer.extractMessage()
    const dependentIds = analyzer.extractDependencies()
    await axios.put(`/api/todo-items/${id}`, {
      message: extractedMessage,
      dependentIds,
    })
  },
  async completeTodoItem({ commit }, { id }) {
    try {
      commit(TodoMutations.INIT_WARN)
      await axios.post(`/api/todo-items/${id}/completion`)
    } catch (error) {
      commit(TodoMutations.WARN, { warn: error.response.data })
    }
  },
  async incompleteTodoItem({ commit }, { id }) {
    commit(TodoMutations.INIT_WARN)
    try {
      await axios.delete(`/api/todo-items/${id}/completion`)
    } catch (error) {
      commit(TodoMutations.WARN, { warn: error.response.data })
    }
  },
  async deleteTodItem({ commit }, { id }) {
    commit(TodoMutations.INIT_WARN)
    try {
      await axios.delete(`/api/todo-items/${id}`)
    } catch (error) {
      commit(TodoMutations.WARN, { warn: error.response.data })
    }
  },
}
