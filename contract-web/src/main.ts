import { createApp } from 'vue'
import { createPinia } from 'pinia'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css'
import App from './App.vue'
import router from './router'
// 生产环境不加载 Mock（通过 Vite 代理到后端）
if (import.meta.env.VITE_USE_MOCK === 'true') {
  import('./mock')
}

const app = createApp(App)
app.config.errorHandler = (err, _instance, info) => {
  console.error('Vue Error:', err, info)
}
app.use(createPinia())
app.use(router)
app.use(Antd)
app.mount('#app')
