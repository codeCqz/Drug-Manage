const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  roles: state => state.user.roles,
  user: state=> state.user.user,
  role: state => state.user.role,
  permission_routes: state => state.permission.routes,//动态路由
  visitedViews: state=>state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews
}
export default getters
