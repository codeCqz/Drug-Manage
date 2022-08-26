<template>
  <el-form>
    <el-form-item label="真实姓名">
      <el-input v-model.trim="temp.realname" />
    </el-form-item>
    <el-form-item label="手机号">
      <el-input v-model.trim="temp.tel" />
    </el-form-item>

    <el-form-item label="地址">
      <el-input v-model.trim="temp.address" />
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="submit">更改</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { update } from "@/api/user";
export default {
  data() {
    return {
      user: "",
      temp: {
        userid: "",
        realname: "",
        tel: "",
        address: "",
      },
    };
  },
  mounted() {
    this.user = this.$store.getters.user;
    this.temp.userid = this.user.userid;
    this.temp.realname = this.user.realname;
    this.temp.tel = this.user.tel;
    this.temp.address = this.user.address;
  },
  methods: {
    reGetInfo() {
      this.temp.userid = this.user.userid;
      this.temp.realname = this.user.realname;
      this.temp.tel = this.user.tel;
      this.temp.address = this.user.address;
    },

    submit() {
      update(this.temp).then((res) => {
        this.user = res.data.userInfo;
        this.$message({
          message: "用户信息更改成功",
          type: "success",
          duration: 5 * 1000,
        });
        this.reGetInfo();
        store.dispatch("user/getInfo");
      });
    },
  },
};
</script>
