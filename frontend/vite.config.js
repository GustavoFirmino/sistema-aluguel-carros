import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      // Repassa chamadas /clientes, /automoveis, /pedidos para o backend Micronaut
      '/clientes':   { target: 'http://localhost:8080', changeOrigin: true },
      '/automoveis': { target: 'http://localhost:8080', changeOrigin: true },
      '/pedidos':    { target: 'http://localhost:8080', changeOrigin: true },
      '/agentes':    { target: 'http://localhost:8080', changeOrigin: true },
      '/contratos':  { target: 'http://localhost:8080', changeOrigin: true },
      '/health':     { target: 'http://localhost:8080', changeOrigin: true },
    },
  },
})
