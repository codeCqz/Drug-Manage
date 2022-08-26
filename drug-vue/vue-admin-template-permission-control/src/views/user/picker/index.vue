<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.username"
        placeholder="请输入用户名"
        style="width: 200px; margin-left: 10px"
        class="filter-item"
        clearable
      />

      <el-input
        v-model="listQuery.realname"
        placeholder="请输入真实姓名"
        style="width: 200px; margin-left: 10px"
        class="filter-item"
        clearable
      />

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

      <el-button type="primary" icon="el-icon-edit" @click="handleCreate"
        >新增用户</el-button
      >
    </div>
    <!-- 上面是添加的按钮 -->

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
        label="用户编号"
        prop="userid"
        sortable="custom"
        align="center"
        width="120"
        :class-name="getSortClass('userid')"
      >
        <template slot-scope="{ row }">
          <span>{{ row.userid }}</span>
        </template>
      </el-table-column>

      <el-table-column label="头像" prop="avatar" align="center" width="120">
        <template slot-scope="{ row }">
          <img
            :src="row.avatar"
            class="user-avatar"
            style="width: 40px; height: 40px; border-radius: 50%"
          />
        </template>
      </el-table-column>

      <el-table-column label="用户名" prop="username" align="center" width="120">
        <template slot-scope="{ row }">
          <span>{{ row.username }}</span>
        </template>
      </el-table-column>

      <el-table-column label="密码" prop="password" align="center" width="120">
        <template slot-scope="{ row }">
          <span>{{ row.password }}</span>
        </template>
      </el-table-column>

      <el-table-column label="手机号" width="120px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.tel }}</span>
        </template>
      </el-table-column>

      <el-table-column label="真实姓名" width="100px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.realname }}</span>
        </template>
      </el-table-column>

      <el-table-column label="身份证号" min-width="180px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.identitycard }}</span>
        </template>
      </el-table-column>
      <el-table-column label="地址" width="220px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.address }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="操作"
        align="center"
        width="180"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="{ row, $index }">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            修改
          </el-button>
          <el-button
            v-if="row.status != 'deleted'"
            size="mini"
            type="danger"
            @click="beforeDelete(row, $index)"
          >
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
        <!-- <el-form-item label="用户ID" prop="userid" hidden>
          <el-input placeholder="请输入用户名称" v-model="temp.userid"></el-input>
        </el-form-item> -->

        <el-form-item label="用户名" prop="username">
          <el-input placeholder="请输入用户名" v-model="temp.username"></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input placeholder="请输入密码" v-model="temp.password"></el-input>
        </el-form-item>
        <el-form-item label="手机号码" prop="tel">
          <el-input placeholder="请输入手机号码" v-model="temp.tel"></el-input>
        </el-form-item>

        <el-form-item label="真实姓名" prop="realname">
          <el-input placeholder="请输入真实姓名" v-model="temp.realname"></el-input>
        </el-form-item>

        <el-form-item label="身份证" prop="identitycard">
          <el-input placeholder="请输入身份证号码" v-model="temp.identitycard"></el-input>
        </el-form-item>

        <el-form-item label="角色" prop="role" v-if="this.temp.userid">
          <el-select
            v-model="temp.editId"
            clearable
            placeholder="请选择"
            @change="changeRole"
          >
            <el-option
              v-for="item in options"
              :key="item.roleid"
              :label="item.remark"
              :value="item.roleid"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="地址" prop="address">
          <el-input placeholder="请输入地址" v-model="temp.address"></el-input>
        </el-form-item>

        <el-form-item label="头像" prop="avatar">
          <img
            :src="temp.avatar"
            class="user-avatar"
            style="width: 40px; height: 40px; border-radius: 50%"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="createData"> 确定 </el-button>
        <el-button @click="cancel"> 取消 </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import ImageCropper from "@/components/ImageCropper";
import PanThumb from "@/components/PanThumb";
import waves from "@/directive/waves"; // waves directive
import Pagination from "@/components/Pagination";
import {
  getBuyerUsers,
  insertUser,
  updateUser,
  deleteUser,
  fetchroleInfoList,
  getSearchUser,
} from "@/api/roledata";

export default {
  name: "Purchase",
  components: { Pagination, ImageCropper, PanThumb },
  directives: { waves },
  filters: {
    typeFilter(type) {
      return calendarTypeKeyValue[type];
    },
  },
  data() {
    const checkTelephone = (rule, value, callback) => {
      if (!this.temp.tel || this.temp.tel.length <= 0) {
        return callback(new Error("请填入手机号！"));
      } else if (this.temp.tel.length < 11 || this.temp.tel.length > 11) {
        return callback(new Error("请填入正确的手机号！"));
      }
      return callback();
    };
    const checkic = (rule, value, callback) => {
      if (!this.temp.identitycard || this.temp.identitycard.length <= 0) {
        return callback(new Error("请填入身份证号！！"));
      } else if (
        this.temp.identitycard.length < 18 ||
        this.temp.identitycard.length > 18
      ) {
        return callback(new Error("请填入正确的身份证号！"));
      }
      return callback();
    };
    return {
      options: [],
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
        username: "",
        realname: "",
        roleId: 2,
        page: 1,
        limit: 10,
        sort: "+userid",
      },
      selectOptions: [],
      selectedOptions: [],
      importanceOptions: [1, 2, 3],
      showReviewer: false,
      date: [],
      temp: {
        userid: "",
        username: "",
        password: "",
        tel: "",
        realname: "",
        identitycard: "",
        address: "",
        avatar: "http://localhost:8082/img/mr.jpeg",
        roleId: 2,
        editId: 2,
      },
      dialogFormVisible: false,
      dialogFormVisible2: false,
      dialogStatus: "",
      textMap: {
        update: "修改用户信息",
        create: "添加用户信息",
      },
      dialogPvVisible: false,
      pvData: [],
      options: [],
      rules: {
        username: [{ required: true, message: "请填写用户名", trigger: "blur" }],
        password: [{ required: true, message: "请填写密码", trigger: "blur" }],
        realname: [{ required: true, message: "请填写真实姓名", trigger: "blur" }],
        role: [{ required: true, message: "请选择用户的角色", trigger: "blur" }],
        tel: [{ required: true, trigger: "blur", validator: checkTelephone }],
        address: [{ required: true, message: "请填写用户的地址", trigger: "blur" }],
        identitycard: [{ required: true, trigger: "blur", validator: checkic }],
      },
      downloadLoading: false,
      rowedit: {
        supplyid: "",
        drugname: "",
      },
    };
  },
  created() {
    this.getList();
  },
  mounted() {
    this.getoptions();
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
    changeRole() {
      this.$forceUpdate();
    },

    getoptions() {
      fetchroleInfoList().then((res) => {
        this.options = res.data.allrole;
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
      return (druglist) => {
        return druglist.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0;
      };
    },

    cancel() {
      this.dialogFormVisible = false;
      this.resetTemp;
    },
    handleChange() {},
    //获取列表数据
    getList() {
      this.listLoading = false;

      getBuyerUsers(this.listQuery).then((response) => {
        this.list = JSON.parse(JSON.stringify(response.data.alluser));
        this.total = response.data.total;
        setTimeout(() => {
          this.listLoading = false;
        }, 1.5 * 1000);
      });
    },

    handleFilter() {
      this.listQuery.page = 1;
      this.getList();
    },
    search() {
      //fetchSearchList
      getSearchUser(this.listQuery).then((response) => {
        this.referenceList = response.data.items;
        if (response.data.total != 0) {
        } else {
          this.$notify({
            title: "提示",
            message: "未搜索到",
            type: "warning",
            duration: 2000,
          });
        }
        const replaceList = this.forReplace(
          JSON.parse(JSON.stringify(response.data.items))
        );
        this.list = replaceList;
        this.total = response.data.total;
      });
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: "操作Success",
        type: "success",
      });
      row.status = status;
    },
    sortChange(data) {
      const { prop, order } = data;
      if (prop === "userid") {
        this.sortByID(order);
      }
    },
    sortByID(order) {
      if (order === "ascending") {
        this.listQuery.sort = "+userid";
      } else {
        this.listQuery.sort = "-userid";
      }
      this.handleFilter();
    },
    resetTemp() {
      this.date = "";
      this.temp = {
        userid: "",
        username: "",
        password: "",
        tel: "",
        realname: "",
        identitycard: "",
        address: "",
        avatar: "http://localhost:8082/img/mr.jpeg",
        roleId: 2,
        editId: 2,
      };
    },

    handleCreate() {
      this.resetTemp();
      this.dialogStatus = "create";

      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].clearValidate();
      });
    },
    createData() {
      this.$refs["dataForm"].validate((valid) => {
        if (valid) {
          this.dialogFormVisible = false;

          if (this.dialogStatus === "update") {
            updateUser(this.temp).then(() => {
              this.getList();
              this.$notify({
                title: "修改成功",
                message: "修改成功",
                type: "success",
                duration: 2000,
              });
            });
          } else if (this.dialogStatus === "create") {
            this.temp.handler = this.$store.getters.user;

            insertUser(this.temp).then(() => {
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
      this.resetTemp();
      this.temp = Object.assign({}, row); // copy obj
      this.temp.editId = 3;
      this.dialogStatus = "update";
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].clearValidate();
      });
    },

    handleDelete(row, index) {
      this.list.splice(index, 1);
      this.userid = row.userid;
      deleteUser(this.userid).then(() => {
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
