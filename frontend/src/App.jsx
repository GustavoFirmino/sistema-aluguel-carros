import { useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import HomePage from './pages/HomePage';
import Login from './pages/Login';
import Cadastro from './pages/Cadastro';
import Perfil from './pages/Perfil';
import MeusPedidos from './pages/MeusPedidos';
import Dashboard from './pages/Dashboard';
import Clientes from './pages/Clientes';
import Automoveis from './pages/Automoveis';
import Pedidos from './pages/Pedidos';
import Agentes from './pages/Agentes';
import Contratos from './pages/Contratos';
import Estrategia from './pages/Estrategia';
import PedidosBanco from './pages/PedidosBanco';
import PedidosEmpresa from './pages/PedidosEmpresa';
import { authService } from './services/api';

const TOAST_OPTS = {
  duration: 3500,
  style: { fontSize: '.875rem', fontFamily: 'Inter, system-ui, sans-serif' },
  success: { style: { background: '#f0fdf4', color: '#15803d', border: '1px solid #bbf7d0' } },
  error:   { style: { background: '#fef2f2', color: '#dc2626', border: '1px solid #fecaca' } },
};

export default function App() {
  const [usuario, setUsuario] = useState(() => {
    try { return JSON.parse(localStorage.getItem('auth_user')); }
    catch { return null; }
  });

  const handleLogin = (u) => {
    setUsuario(u);
    localStorage.setItem('auth_user', JSON.stringify(u));
  };

  const handleLogout = () => {
    const token = localStorage.getItem('auth_token');
    if (token) authService.logout().catch(() => {});
    localStorage.removeItem('auth_token');
    localStorage.removeItem('auth_user');
    setUsuario(null);
  };

  const role = usuario?.role;
  const isAdmin   = role === 'admin' || role === 'agente';
  const isBanco   = role === 'banco';
  const isEmpresa = role === 'empresa';
  const isCliente = role === 'cliente';

  // Destino após login por papel
  const homeByRole = isAdmin ? '/dashboard' : isBanco ? '/banco/pedidos' : isEmpresa ? '/empresa/pedidos' : '/';

  return (
    <BrowserRouter>
      <Toaster position="top-right" toastOptions={TOAST_OPTS} />
      <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column', background: '#f8fafc' }}>
        <Navbar usuario={usuario} onLogout={handleLogout} />
        <div style={{ flex: 1 }}>
          <Routes>

            {/* ── Público ── */}
            <Route path="/" element={<HomePage usuario={usuario} />} />
            <Route path="/login"
              element={!usuario
                ? <Login onLogin={handleLogin} />
                : <Navigate to={homeByRole} replace />} />
            <Route path="/cadastro"
              element={!usuario
                ? <Cadastro onLogin={handleLogin} />
                : <Navigate to={isCliente ? '/perfil' : homeByRole} replace />} />

            {/* ── Cliente ── */}
            <Route path="/perfil"
              element={isCliente ? <Perfil usuario={usuario} /> : <Navigate to="/login" replace />} />
            <Route path="/meus-pedidos"
              element={isCliente ? <MeusPedidos usuario={usuario} /> : <Navigate to="/login" replace />} />

            {/* ── Admin / Agente ── */}
            <Route path="/dashboard"
              element={isAdmin ? <Dashboard /> : <Navigate to={homeByRole} replace />} />
            <Route path="/clientes"
              element={isAdmin ? <Clientes /> : <Navigate to={homeByRole} replace />} />
            <Route path="/pedidos"
              element={isAdmin ? <Pedidos /> : <Navigate to={homeByRole} replace />} />
            <Route path="/agentes"
              element={role === 'admin' ? <Agentes /> : <Navigate to={homeByRole} replace />} />

            {/* ── Backoffice compartilhado ── */}
            <Route path="/automoveis"
              element={(isAdmin || isBanco || isEmpresa) ? <Automoveis /> : <Navigate to="/" replace />} />
            <Route path="/contratos"
              element={(isAdmin || isEmpresa) ? <Contratos /> : <Navigate to={homeByRole} replace />} />

            {/* ── Banco ── */}
            <Route path="/banco/pedidos"
              element={isBanco ? <PedidosBanco usuario={usuario} /> : <Navigate to={homeByRole} replace />} />

            {/* ── Empresa ── */}
            <Route path="/empresa/pedidos"
              element={isEmpresa ? <PedidosEmpresa usuario={usuario} /> : <Navigate to={homeByRole} replace />} />

            {/* ── Público ── */}
            <Route path="/estrategia" element={<Estrategia />} />

            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </div>
        <Footer />
      </div>
    </BrowserRouter>
  );
}
