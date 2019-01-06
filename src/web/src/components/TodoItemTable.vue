<template>
  <v-data-table :headers="headers" :items="items" class="elevation-1">
    <template slot="items" slot-scope="props">
      <td>{{ props.item.name }}</td>
      <td class="text-xs-right">{{ props.item.calories }}</td>
      <td class="text-xs-right">{{ props.item.fat }}</td>
      <td class="text-xs-right">{{ props.item.carbs }}</td>
      <td class="text-xs-right">{{ props.item.protein }}</td>
      <td class="text-xs-right">{{ props.item.iron }}</td>
    </template>
  </v-data-table>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import axios from 'axios'

@Component
export default class TodoItemTable extends Vue {
  private headers = [
    { text: 'ID', align: 'center', sortable: false, value: 'name' },
    { text: '할 일', align: 'left', sortable: false, value: 'calories' },
    { text: '작성일시', align: 'center', sortable: false, value: 'fat' },
    { text: '최종수정일시', align: 'center', sortable: false, value: 'carbs' },
    { text: '완료여부', align: 'center', sortable: false, value: 'protein' },
    { text: '완료일시', align: 'center', sortable: false, value: 'iron' },
  ]
  private items = []
  private page = 0
  private perpage = 10

  private mounted() {
    this.fetchItems()
  }

  private fetchItems() {
    const offset = 0
    const count = this.perpage
    
    axios
      .get(`/api/todo-items?offset=${offset}&count=${count}`)
      .then((response) => {
        console.log(response)
      })
      .catch((error) => {
        console.log(error)
      })
  }
}
</script>