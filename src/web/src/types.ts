export interface TodoItem {
  id: number
  message: string,
  dependentIds: number[]
  writtenAt: Date
  modifiedAt: Date
  completed: Date
}
