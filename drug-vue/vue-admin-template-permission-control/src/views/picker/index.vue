<template>
  <div class="app-container">
    <div class="filter-container">
      <el-autocomplete
        class="inline-input"
        v-model="listQuery.drugname"
        :fetch-suggestions="querySearch"
        placeholder="请输入药品名称"
        @select="handleSelect"
        :debounce="0"
        clearable
      ></el-autocomplete>
      <el-input
        v-model="listQuery.druguser"
        placeholder="请输入患者姓名"
        style="width: 200px; margin-left: 10px"
        class="filter-item"
        clearable
      />

      <!--      &lt;!&ndash; 下面是三个功能按钮 &ndash;&gt;-->
      <el-button
        v-waves
        class="filter-item"
        style="margin-left: 10px"
        type="primary"
        icon="el-icon-search"
        @click="search"
      >
        搜索
      </el-button>

      <div v-if="this.$store.getters.role.role != 'admin'" style="display: inline-block">
        <el-button
          type="primary"
          icon="el-icon-edit"
          @click="handleCreate"
          style="margin-left: 10px"
          >新增</el-button
        >

        <el-button
          v-waves
          :loading="downloadLoading"
          style="margin-left: 10px"
          class="filter-item"
          type="primary"
          icon="el-icon-download"
          @click="handleDownload"
        >
          导出本页
        </el-button>

        <el-button
          v-waves
          :loading="downloadLoading"
          style="margin-left: 10px"
          class="filter-item"
          type="primary"
          icon="el-icon-download"
          @click="handleDownloadAll"
        >
          导出全部
        </el-button>
      </div>
    </div>
    <!-- 上面是搜索框、添加、导出的按钮 -->

    <!-- 下面是表格内容 -->
    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%; margin-top: 30px"
      @sort-change="sortChange"
    >
      <el-table-column
        label="编号"
        prop="id"
        sortable="custom"
        align="center"
        width="130"
        :class-name="getSortClass('id')"
      >
        <template slot-scope="{ row }">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>

      <el-table-column label="药品名称" width="200px" align="center">
        <template slot-scope="{ row }">
          <button
            @click="lookDrug(row)"
            style="border: none; background-color: transparent"
          >
            <span>{{ row.drugname }}</span>
          </button>
        </template>
      </el-table-column>

      <el-table-column label="数量" width="130px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.quantity }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作者ID" width="130px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.handler }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作时间" width="150px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.jointime }}</span>
        </template>
      </el-table-column>

      <el-table-column label="患者姓名" width="130px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.druguser }}</span>
        </template>
      </el-table-column>

      <el-table-column label="过期时间" width="150px" align="center">
        <template slot-scope="{ row }">
          {{ row.exp }}
        </template>
      </el-table-column>

      <el-table-column label="药品单价" width="130px" align="center">
        <template slot-scope="{ row }">
          {{ row.price }}
        </template>
      </el-table-column>

      <el-table-column label="药品总价" width="130px" align="center">
        <template slot-scope="{ row }">
          {{ row.tp }}
        </template>
      </el-table-column>

      <el-table-column
        label="操作"
        align="center"
        width="180"
        class-name="small-padding fixed-width"
        v-if="this.$store.getters.role.role != 'admin'"
      >
        <template slot-scope="{ row, $index }">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            修改
          </el-button>

          <el-button size="mini" type="danger" @click="beforeDelete(row, $index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 下面是elmentui的分页实现 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />

    <!-- 添加时弹出的框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="120px"
        style="width: 460px; margin-left: 50px"
      >
        <el-form-item label="操作者" prop="handler">
          <el-input v-model="temp.handler" disabled></el-input>
        </el-form-item>

        <div v-if="this.dialogStatus === `create`">
          <el-form-item label="药品名称" prop="drugname">
            <el-autocomplete
              class="inline-input"
              v-model="temp.drugname"
              :fetch-suggestions="querySearch"
              placeholder="请输入药品名称"
              @select="handleSelect"
              :debounce="0"
              clearable
            ></el-autocomplete>
          </el-form-item>

          <el-form-item label="数量" prop="quantity">
            <el-input-number
              v-model="temp.quantity"
              @blur="numBlur"
              @change="numChange"
              :min="1"
              :max="10000"
              label="药品数量"
            ></el-input-number>
          </el-form-item>
          <el-form-item label="创建日期" prop="jointime">
            <div class="block">
              <el-date-picker v-model="jointime" type="date" readonly> </el-date-picker>
            </div>
          </el-form-item>
          <el-form-item label="药品单价" prop="price">
            <el-input v-model="temp.price" readonly></el-input>
          </el-form-item>
        </div>

        <el-form-item
          label="药品过期日期"
          prop="exp"
          v-if="this.dialogStatus === `create`"
        >
          <div class="block">
            <!-- v-model="temp.exp" -->
            <el-select
              v-model="temp.exp"
              placeholder="请选择"
              @focus="getoptions"
              multiple
            >
              <el-option v-for="item in options" :key="item" :label="item" :value="item">
              </el-option>
            </el-select>
          </div>
        </el-form-item>

        <el-form-item label="患者名" prop="druguser">
          <el-input v-model="temp.druguser"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="createData"> 确定 </el-button>
        <el-button @click="cancel"> 取消 </el-button>
      </div>
    </el-dialog>

    <!-- 药品详细信息弹出的框 -->
    <el-dialog title="药品详细信息" :visible.sync="dialogFormVisible2">
      <el-descriptions class="margin-top" title="" column="2" size="small" border>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-s-order"></i>
            药品名称
          </template>
          {{ this.druginfo.drugname }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-tickets"></i>
            批准文号
          </template>
          国药准字{{ this.druginfo.approvalnum }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-office-building"></i>
            生产单位
          </template>
          {{ this.druginfo.company }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-tickets"></i>
            药品本位码
          </template>
          {{ this.druginfo.standardcode }}
          <!-- 详细信息 -->
        </el-descriptions-item>
        <el-descriptions-item v-if="this.druginfo.ingredients">
          <template slot="label">
            <i class="el-icon-coin"></i>
            成份
          </template>
          {{ this.druginfo.ingredients }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.drugclass">
          <template slot="label">
            <i class="el-icon-document-copy"></i>
            分类
          </template>
          {{ this.druginfo.drugclass }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.otc">
          <template slot="label">
            <i class="el-icon-price-tag"></i>
            OTC类型
          </template>
          {{ this.druginfo.otc }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.specifications">
          <template slot="label">
            <i class="el-icon-box"></i>
            规格
          </template>
          {{ this.druginfo.specifications }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.drugtype">
          <template slot="label">
            <i class="el-icon-files"></i>
            药品类型
          </template>
          {{ this.druginfo.drugtype }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.indication">
          <template slot="label">
            <i class="el-icon-document-checked"></i>
            适应症状
          </template>
          {{ this.druginfo.indication }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.usageanddosage">
          <template slot="label">
            <i class="el-icon-water-cup"></i>
            用法用量
          </template>
          {{ this.druginfo.usageanddosage }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.storage">
          <template slot="label">
            <i class="el-icon-takeaway-box"></i>
            贮藏方式
          </template>
          {{ this.druginfo.storage }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.adversereaction">
          <template slot="label">
            <i class="el-icon-collection-tag"></i>
            不良反应
          </template>
          {{ this.druginfo.adversereaction }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.taboo">
          <template slot="label">
            <i class="el-icon-paperclip"></i>
            禁忌
          </template>
          {{ this.druginfo.taboo }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.drugregulatoryclassification">
          <template slot="label">
            <i class="el-icon-s-data"></i>
            药品监管等级
          </template>
          {{ this.druginfo.drugregulatoryclassification }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.functionalindications">
          <template slot="label">
            <i class="el-icon-discount"></i>
            功能主治
          </template>
          {{ this.druginfo.functionalindications }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.character">
          <template slot="label">
            <i class="el-icon-reading"></i>
            性状
          </template>
          {{ this.druginfo.character }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { insertpickdrug, updatepickdrug } from "@/api/pickdrug";
import { fetchGetMedicineList, deleteGetMedicine, getsearch } from "@/api/getmedicine";
import waves from "@/directive/waves"; // waves directive
import Pagination from "@/components/Pagination";
import { getdruginfo } from "@/api/drug.js";
import {
  fetchstoragedrugnameList,
  fetchstoragenumberList,
  fetchExpList,
  fetchexpnum,
  fetchprice,
} from "@/api/storage.js";
export default {
  components: { Pagination },
  directives: { waves },
  filters: {
    typeFilter(type) {
      return calendarTypeKeyValue[type];
    },
  },
  data() {
    const checkNum = (rule, value, callback) => {
      if (!this.temp.drugname || this.temp.drugname.length <= 0) {
        this.$refs.dataForm.validateField("drugname");
      } else {
        if (this.temp.quantity >= 1) {
          if (this.temp.exp.length == this.options.length) {
            fetchstoragenumberList(this.temp.drugname).then((res) => {
              if (this.temp.quantity > res.data.count) {
                return callback(
                  new Error("库存不足,当前选择剩余库存为:" + res.data.count)
                );
              } else if (this.temp.quantity <= 0) {
                return callback(new Error("请填入数量！"));
              } else {
                return callback();
              }
            });
          } else {
            var t = JSON.parse(JSON.stringify(this.temp.exp));

            var f = "";
            for (let index = 0; index < t.length; index++) {
              const element = t[index];
              f += element + "#";
            }
            var json = {
              exp: f,
              drugname: this.temp.drugname,
            };
            fetchexpnum(json).then((res) => {
              if (this.temp.quantity > res.data.count) {
                return callback(
                  new Error("库存不足,当前选择剩余库存为:" + res.data.count)
                );
              } else if (this.temp.quantity <= 0) {
                return callback(new Error("请填入数量！"));
              } else {
                return callback();
              }
            });
          }
        }
      }
    };
    const checkExp = (rule, value, callback) => {
      if (!this.temp.exp) {
        return callback(new Error("请填入日期！"));
      } else {
      }
      return callback();
    };
    const checkDrugname = (rule, value, callback) => {
      if (!this.temp.drugname || this.temp.drugname.length <= 0) {
        return callback(new Error("请输入药品名称！"));
      } else if (this.temp.drugname || this.temp.drugname.length > 0) {
        fetchstoragenumberList(this.temp.drugname).then((res) => {
          if (res.data.flag === 0) {
            return callback(new Error("请输入正确的药品名称！"));
          }
        });
      } else {
        this.getPrice();
      }
      return callback();
    };
    return {
      drugflag: 0,
      stock: 0,
      statusopt: ["已入库", "未入库"],
      referenceList: [],
      supplyid: "",
      tableKey: 0,
      edittime: "",
      jointime: "",
      list: null,
      total: 0,
      listLoading: false,
      druglist: [],
      listQuery: {
        druguser: "",
        drugname: "",
        downloadAll: false,
        page: 1,
        limit: 10,
        sort: "+id",
      },
      selectOptions: [],
      selectedOptions: [],
      importanceOptions: [1, 2, 3],
      showReviewer: false,
      date: [],
      temp: {
        drugname: "",
        handler: this.$store.getters.user.userid,
        quantity: 0,
        druguser: "",
        jointime: "",
        exp: [],
        price: "",
      },
      dialogFormVisible: false,
      dialogFormVisible2: false,
      dialogStatus: "",
      textMap: {
        update: "修改取药信息",
        create: "添加取药信息",
      },
      dialogPvVisible: false,
      pvData: [],
      options: [],
      rules: {
        drugname: [{ required: true, trigger: "blur", validator: checkDrugname }],
        quantity: [{ required: true, trigger: "change", validator: checkNum }],
        druguser: [{ required: true, trigger: "blur", message: "请输入患者姓名" }],
      },
      druginfo: {
        drugid: "",
        drugname: "无",
        approvalnum: "",
        company: "",
        standardcode: "",
        ingredients: "",
        drugclass: "",
        otc: "",
        specifications: "",
        drugtype: "",
        indication: "",
        usageanddosage: "",
        storage: "",
        adversereaction: "",
        taboo: "",
        drugregulatoryclassification: "",
        functionalindications: "",
        character: "",
      },
      downloadLoading: false,
    };
  },
  watch: {
    temp: {
      deep: true,
      handler() {
        if (!this.temp.drugname || this.temp.drugname.length <= 0) {
        } else if (this.temp.drugname || this.temp.drugname.length > 0) {
          fetchstoragenumberList(this.temp.drugname).then((res) => {
            if (res.data.flag === 0) {
            } else {
              this.getPrice();
            }
          });
        } else {
          this.getPrice();
        }
      },
    },
  },
  created() {
    this.getList();
  },
  mounted() {
    this.temp.handler = this.$store.getters.user.userid;
    fetchstoragedrugnameList().then((res) => {
      this.druglist = res.data.drugname;
    });
  },
  methods: {
    beforeDelete(row, index) {
      this.$confirm("此操作将删除记录，确认删除吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "error",
      }).then(() => {
        this.handleDelete(row, index);
      });
    },

    getPrice() {
      fetchprice(this.temp.drugname).then((res) => {
        this.temp.price = res.data.price;
      });
    },

    getoptions() {
      fetchExpList(this.temp.drugname).then((res) => {
        console.log(this.temp.drugname);
        this.options = res.data.explist;
        console.log(res.data);
      });
    },

    lookDrug(row) {
      getdruginfo(row.drugname).then((res) => {
        this.druginfo = res.data.druginfo;
        this.dialogFormVisible2 = true;
      });
    },
    drugtime() {
      this.temp.exp = this.formatDate(this.temp.exp, "yyyy-MM-dd");
    },

    querySearch(queryString, cb) {
      var druglist = this.druglist;
      var results = queryString
        ? druglist.filter(this.createFilter(queryString))
        : druglist;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return (druglist) =>
        druglist.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0;
    },
    handleSelect(item) {
      let query = JSON.parse(JSON.stringify(item.value));
      this.temp.drugname = query;
      this.$refs.dataForm.validateField("drugname");
    },

    numBlur() {
      this.$refs.dataForm.validateField("quantity");
    },

    numChange(value) {
      this.temp.quantity = value;
    },

    changeList() {
      this.getList();
    },

    cancel() {
      this.dialogFormVisible = false;
      this.resetTemp;
    },
    //获取列表数据
    getList() {
      this.listLoading = false;
      if (this.listQuery.drugname != "" || this.listQuery.druguser != "") {
        this.search();
      } else {
        fetchGetMedicineList(this.listQuery).then((response) => {
          var templist = JSON.parse(JSON.stringify(response.data.allpick));
          this.total = response.data.total;

          this.list = templist;
          setTimeout(() => {
            this.listLoading = false;
          }, 1.5 * 1000);
        });
      }
    },

    handleFilter() {
      this.listQuery.page = 1;
      this.getList();
    },
    search() {
      //fetchSearchList
      getsearch(this.listQuery).then((response) => {
        if (response.data.total != 0) {
        } else {
          this.$notify({
            title: "提示",
            message: "未搜索到",
            type: "warning",
            duration: 2000,
          });
        }
        this.list = response.data.items;
        this.total = response.data.total;
      });
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: "操作Success",
        type: "success",
      });
    },
    sortChange(data) {
      const { prop, order } = data;
      if (prop === "id") {
        this.sortByID(order);
      }
    },
    sortByID(order) {
      if (order === "ascending") {
        this.listQuery.sort = "+id";
      } else {
        this.listQuery.sort = "-id";
      }
      this.handleFilter();
    },
    resetTemp() {
      this.date = "";
      this.temp = {
        drugname: "",
        handler: this.$store.getters.user.userid,
        quantity: 0,
        druguser: "",
        jointime: "",
        exp: [],
        price: "",
      };
    },

    handleCreate() {
      this.resetTemp();
      this.dialogStatus = "create";
      var nowDate = new Date();
      nowDate = this.formatDate(nowDate, "yyyy-MM-dd");

      this.jointime = nowDate;
      this.temp.jointime = this.jointime;
      this.dialogFormVisible = true;

      this.$nextTick(() => {
        this.$refs["dataForm"].clearValidate();
      });
    },
    createData() {
      this.$refs["dataForm"].validate((valid) => {
        if (valid) {
          this.dialogFormVisible = false;

          var t = JSON.parse(JSON.stringify(this.temp.exp));

          if (t.length > 0) {
            var f = "";
            for (let index = 0; index < t.length; index++) {
              const element = t[index];
              f += element + "#";
            }

            this.temp.exp = f;
          }

          if (this.dialogStatus === "update") {
            var pd = this.temp;
            var data = {
              pd,
            };
            updatepickdrug(data).then(() => {
              this.getList();
              this.$notify({
                title: "修改成功",
                message: "修改成功",
                type: "success",
                duration: 2000,
              });
            });
          } else if (this.dialogStatus === "create") {
            var pd = this.temp;
            var data = {
              pd,
            };

            insertpickdrug(data).then(() => {
              this.getList();
              this.$notify({
                title: "添加成功",
                message: "添加成功",
                type: "success",
                duration: 2000,
              });
            });
          }
        }
      });
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row); // copy obj

      var nowDate = new Date();
      nowDate = this.formatDate(nowDate, "yyyy-MM-dd");

      this.dialogStatus = "update";
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].clearValidate();
      });
    },

    handleDelete(row, index) {
      this.list.splice(index, 1);
      this.id = row.id;
      deleteGetMedicine(this.id).then(() => {
        this.getList();
        this.$notify({
          title: "成功",
          message: "删除成功",
          type: "success",
          duration: 2000,
        });
      });
    },
    handleFetchPv(pv) {
      fetchPv(pv).then((response) => {
        this.pvData = response.data.pvData;
        this.dialogPvVisible = true;
      });
    },
    handleDownload() {
      import("@/vendor/Export2Excel").then((excel) => {
        const tHeader = ["编号", "药品名称", "数量", "操作者ID", "取药时间", "患者名"];
        const filterVal = [
          "id",
          "drugname",
          "quantity",
          "handler",
          "jointime",
          "druguser",
        ];
        const listdata = this.list;
        const data = this.formatJson(filterVal, listdata);
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: "取药信息-第" + this.listQuery.page + "页",
        });
      });
    },

    handleDownloadAll() {
      this.listQuery.downloadAll = true;
      fetchGetMedicineList(this.listQuery).then((response) => {
        this.listQuery.downloadAll = false;
        import("@/vendor/Export2Excel").then((excel) => {
          const tHeader = ["编号", "药品名称", "数量", "操作者ID", "取药时间", "患者名"];
          const filterVal = [
            "id",
            "drugname",
            "quantity",
            "handler",
            "jointime",
            "druguser",
          ];
          const listdata = response.data.allpick;
          const data = this.formatJson(filterVal, listdata);
          excel.export_json_to_excel({
            header: tHeader,
            data,
            filename: "取药信息(全部)",
          });
          // this.downloadLoading = true
        });
      });
    },

    formatJson(filterVal, listdata) {
      return listdata.map((v) => filterVal.map((j) => v[j]));
    },
    getSortClass: function (key) {
      const sort = this.listQuery.sort;
      return sort === `+${key}` ? "ascending" : "descending";
    },
    formatDate(date, fmt) {
      if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(
          RegExp.$1,
          (date.getFullYear() + "").substr(4 - RegExp.$1.length)
        );
      }
      let o = {
        "M+": date.getMonth() + 1,
        "d+": date.getDate(),
        "h+": date.getHours(),
        "m+": date.getMinutes(),
        "s+": date.getSeconds(),
      };
      for (let k in o) {
        if (new RegExp(`(${k})`).test(fmt)) {
          let str = o[k] + "";
          fmt = fmt.replace(
            RegExp.$1,
            RegExp.$1.length === 1 ? str : this.padLeftZero(str)
          );
        }
      }
      return fmt;
    },

    padLeftZero(str) {
      return ("00" + str).substr(str.length);
    },
  },
};
</script>
