import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8090',
        changeOrigin: true,
        secure: false,
      },
    },
  },
  optimizeDeps: {
    include: [
      '@fullcalendar/react',
      '@fullcalendar/timegrid',
      '@fullcalendar/interaction',
      '@fullcalendar/daygrid',
    ],
  },
});