import { TodoItem } from '@/types'

export class TodoMutations {
  public static readonly FETCH_TODOLIST = 'FETCH_TODOLIST'
  public static readonly FETCH_TODOLIST_SUCCEED = 'FETCH_TODOLIST_SUCCEED'
  public static readonly FETCH_TODOLIST_FAILED = 'FETCH_TODOLIST_FAILED'
}

export interface TodoState {
  offset: number
  rowsPerPage: number
  totalCount: number
  todolist: TodoItem[]
  error: string | null
}
