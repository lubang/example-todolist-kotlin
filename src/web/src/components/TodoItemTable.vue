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
        <td>{{ props.item.message }}</td>
        <td>
          <v-checkbox hide-details v-model="props.item.completed"></v-checkbox>
        </td>
        <td class="text-xs-center">{{ toHumanReadableTime(props.item.writtenAt) }}</td>
        <td class="text-xs-center">{{ toHumanReadableTime(props.item.modifiedAt) }}</td>
        <td class="text-xs-center">{{ toHumanReadableTime(props.item.completedAt) }}</td>
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
      text: '완료여부',
      align: 'center',
      sortable: false,
      value: 'completed',
      width: '80',
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
}
</script>