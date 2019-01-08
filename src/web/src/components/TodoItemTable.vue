<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="todolist"
      no-data-text="등록된 할 일이 없습니다."
      hide-actions
      class="elevation-1"
    >
      <template slot="items" slot-scope="props">
        <td class="text-xs-right">{{ props.item.id }}</td>
        <td>
          <v-dialog v-model="props.item.dialog" width="500">
            <span slot="activator">
              {{ props.item.message }}
              <span
                class="green--text ml-1"
                :key="did"
                v-for="did in props.item.dependentIds"
              >@{{did}}</span>
            </span>
            <v-card>
              <v-card-title class="headline grey lighten-2" primary-title>할 일 수정하기</v-card-title>
              <v-card-text>
                <v-text-field
                  v-model="props.item.message"
                  @keypress.enter.prevent="saveMessage(props.item.id, props.item.message, props.item.dialog)"
                ></v-text-field>
              </v-card-text>
              <v-divider></v-divider>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                  color="primary"
                  @click.stop="saveMessage(props.item.id, props.item.message, props.item.dialog)"
                >수정</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </td>
        <td>
          <v-checkbox
            hide-details
            v-model="props.item.completed"
            @click.stop="toggleTodoItemCompletion(props.item.id, props.item.completed)"
          ></v-checkbox>
        </td>
        <td class="text-xs-left">{{ toHumanReadableTime(props.item.writtenAt) }}</td>
        <td class="text-xs-left">{{ toHumanReadableTime(props.item.modifiedAt) }}</td>
        <td class="text-xs-left">{{ toHumanReadableTime(props.item.completedAt) }}</td>
        <td>
          <v-icon small @click.stop="deleteTodoItem(props.item.id)">delete</v-icon>
        </td>
      </template>
    </v-data-table>
    <v-layout class="pt-4">
      <v-flex sm2>
        <v-select
          :items="rowsPerPageTypes"
          v-model="rowsPerPage"
          label="페이지 당 표시건수"
          class="rows-per-page"
        ></v-select>
      </v-flex>
      <v-flex class="text-xs-center">
        <v-pagination v-model="page" :length="totalPage" :total-visible="8"></v-pagination>
      </v-flex>
    </v-layout>
    <v-layout class="mt-4 pa-3 white elevation-1">
      <h4>사용법</h4>
      <ul class="ml-4">
        <li>우상단 '새 할 일 추가'를 눌러서 할 일을 등록합니다.</li>
        <li>표의 '완료' ㅁ를 눌러서 할 일을 완료합니다.</li>
        <li>표의 '할 일'을 누르면 내용을 수정할 수 있습니다.</li>
        <li>할 일 의존성은 '할 일' 내용 끝에 '@1, @2, @{id}' 형태로 작성하여 등록합니다. 변경 시에도 @1, @2와 같이 입력하면 변경됩니다. @를 사용하지 않는 경우에는 기존의 값이 유지됩니다.</li>
      </ul>
    </v-layout>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator'
import { TodoItem } from '@/types'
import moment from 'moment'

@Component
export default class TodoItemTable extends Vue {
  private headers = [
    { text: 'ID', align: 'center', sortable: false, value: 'id', width: '80' },
    { text: '할 일', align: 'left', sortable: false, value: 'message' },
    {
      text: '완료',
      align: 'center',
      sortable: false,
      value: 'completed',
      width: '60',
    },
    {
      text: '작성일시',
      align: 'left',
      sortable: false,
      value: 'writtenAt',
      width: '265',
    },
    {
      text: '최종수정일시',
      align: 'left',
      sortable: false,
      value: 'modifiedAt',
      width: '265',
    },
    {
      text: '완료일시',
      align: 'left',
      sortable: false,
      value: 'completedAt',
      width: '265',
    },
    {
      text: '동작',
      align: 'left',
      sortable: false,
      value: 'action',
      width: '60',
    },
  ]
  private rowsPerPageTypes = [5, 10, 20, 30, 50, 100]
  private page = 0
  private rowsPerPage = 10

  private get todolist(): TodoItem[] {
    return this.$store.getters.todolist
  }

  private get totalPage(): number {
    return this.$store.getters.totalPage
  }

  private mounted() {
    this.page = 1
  }

  @Watch('page')
  private onPageChanged(value: any, oldValue: any) {
    const offset = (value - 1) * this.rowsPerPage
    this.$store.dispatch('fetchTodolist', {
      offset,
      rowsPerPage: this.rowsPerPage,
    })
  }

  @Watch('rowsPerPage')
  private onRowsPerPageChanged(value: any, oldValue: any) {
    const offset = 0
    this.$store.dispatch('fetchTodolist', {
      offset,
      rowsPerPage: value,
    })
  }

  private toHumanReadableTime(date: Date): string {
    if (date === undefined) {
      return ''
    }
    const dateAt = moment(date)
    return `${dateAt.format('LLL')} (${dateAt.fromNow()})`
  }

  private toggleTodoItemCompletion(id: number, isCompleted: boolean) {
    const action = isCompleted ? 'incompleteTodoItem' : 'completeTodoItem'
    this.$store.dispatch(action, { id }).then(() => {
      this.$store.dispatch('fetchTodolist', {})
    })
  }

  private saveMessage(id: number, message: string, dialog: boolean) {
    this.$store.dispatch('editMessage', { id, message }).then(() => {
      dialog = false
      this.$store.dispatch('fetchTodolist', {})
    })
  }

  private deleteTodoItem(id: number) {
    this.$store.dispatch('deleteTodItem', { id }).then(() => {
      this.$store.dispatch('fetchTodolist', {})
    })
  }
}
</script>