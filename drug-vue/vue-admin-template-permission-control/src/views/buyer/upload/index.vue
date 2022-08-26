<template>
  <div class="app-container">
    <upload-excel-component :on-success="handleSuccess" :before-upload="beforeUpload" />
    <el-table :data="tableData" border highlight-current-row style="width: 100%;margin-top:20px;">
      <el-table-column v-for="item of tableHeader" :key="item" :prop="item" :label="item" />
    </el-table>
  </div>
</template>

<script>
import UploadExcelComponent from '@/components/UploadExcel/index.vue'
import { insertManyDealer } from '@/api/dealer'
export default {
  name: 'UploadExcel',
  components: { UploadExcelComponent },
  data() {
    return {
      tableData: [],
      tableHeader:  [],
      newTable : {},
      queryTable:[]
    }
  },
  methods: {
    beforeUpload(file) {
      const isLt1M = file.size / 1024 / 1024 < 1

      if (isLt1M) {
        return true
      }

      this.$message({
        message: '请不要上传超过1MB的文件!',
        type: 'warning'
      })
      return false
    },
    handleSuccess({ results, header }) {
      this.tableHeader = header  
      this.tableData = results

      for (let index = 0; index < results.length; index++) {  
        this.newTable.dealerid = results[index].编号
        this.newTable.dealername =results[index].名称
        this.newTable.area = results[index].区域
        this.newTable.address = results[index].详细地址
        this.newTable.telephone = results[index].手机号
        this.newTable.contacts = results[index].联系人
        this.newTable.account = results[index].银行卡号
        this.newTable.bank = results[index].银行名称
        this.queryTable[index]=this.newTable
        this.newTable={}   
      } 
      let index = 0
      let copy = -1
      while(index<this.queryTable.length){
        if(index<this.queryTable.length){   
          if(copy==index){
            continue
          }else{
            copy = index  
            insertManyDealer(this.queryTable[index]).then(   
              index++
            )
          }      
        }else{
          break
        }
      }      
    },
  }
}
</script>
