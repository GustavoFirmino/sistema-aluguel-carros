import { useState } from 'react';
import { BrowserRouter, Routes, Route, useLocation } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import Clientes from './pages/Clientes';
import Automoveis from './pages/Automoveis';
import Pedidos from './pages/Pedidos';
import Agentes from './pages/Agentes';
import Contratos from './pages/Contratos';
import Login from './pages/Login';
import { LogOut, User } from 'lucide-react';

const PAGE_TITLES = {
  '/':           { title: 'Dashboard',         sub: 'Visão geral do sistema' },
  '/clientes':   { title: 'Clientes',           sub: 'Gestão de clientes cadastrados' },
  '/automoveis': { title: 'Automóveis',         sub: 'Frota disponível para aluguel' },
  '/pedidos':    { title: 'Pedidos de Aluguel', sub: 'Criação e acompanhamento de pedidos' },
  '/agentes':    { title: 'Agentes',            sub: 'Empresas e bancos que analisam pedidos' },
  '/contratos':  { title: 'Contratos',          sub: 'Contratos gerados a partir de pedidos aprovados' },
};

function Topbar({ usuario, onLogout }) {
  const { pathname } = useLocation();
  const info = PAGE_TITLES[pathname] || PAGE_TITLES['/'];
  return (
    <header className="topbar">
      <div>
        <div className="topbar-title">{info.title}</div>
        <div className="topbar-subtitle">{info.sub}</div>
      </div>
      <div style={{ display: 'flex', alignItems: 'center', gap: '.75rem' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '.4rem', fontSize: '.82rem', color: '#6b7280' }}>
          <User size={15} />
          <span style={{ fontWeight: 600, color: '#374151' }}>{usuario?.nome}</span>
        </div>
        <button
          onClick={onLogout}
          title="Sair"
          style={{
            display: 'flex', alignItems: 'center', gap: '.3rem',
            padding: '.35rem .7rem', borderRadius: 6,
            background: 'transparent', border: '1px solid #e5e7eb',
            cursor: 'pointer', fontSize: '.78rem', color: '#6b7280',
            transition: 'all .15s',
          }}
          onMouseEnter={e => { e.currentTarget.style.background = '#fef2f2'; e.currentTarget.style.color = '#dc2626'; e.currentTarget.style.borderColor = '#fca5a5'; }}
          onMouseLeave={e => { e.currentTarget.style.background = 'transparent'; e.currentTarget.style.color = '#6b7280'; e.currentTarget.style.borderColor = '#e5e7eb'; }}
        >
          <LogOut size={13} /> Sair
        </button>
      </div>
    </header>
  );
}

function AppLayout({ usuario, onLogout }) {
  return (
    <div className="layout">
      <Sidebar />
      <div className="main">
        <Topbar usuario={usuario} onLogout={onLogout} />
        <Routes>
          <Route path="/"           element={<Dashboard />} />
          <Route path="/clientes"   element={<Clientes />} />
          <Route path="/automoveis" element={<Automoveis />} />
          <Route path="/pedidos"    element={<Pedidos />} />
          <Route path="/agentes"    element={<Agentes />} />
          <Route path="/contratos"  element={<Contratos />} />
        </Routes>
      </div>
    </div>
  );
}

export default function App() {
  const [usuario, setUsuario] = useState(() => {
    try { return JSON.parse(localStorage.getItem('auth_user')); }
    catch { return null; }
  });

  const handleLogin = (u) => setUsuario(u);

  const handleLogout = () => {
    localStorage.removeItem('auth_user');
    setUsuario(null);
  };

  if (!usuario) {
    return (
      <>
        <Login onLogin={handleLogin} />
        <Toaster position="top-right" />
      </>
    );
  }

  return (
    <BrowserRouter>
      <AppLayout usuario={usuario} onLogout={handleLogout} />
      <Toaster
        position="top-right"
        toastOptions={{
          duration: 3500,
          style: { fontSize: '.875rem', fontFamily: 'Inter, system-ui, sans-serif' },
          success: { style: { background: '#f0fdf4', color: '#15803d', border: '1px solid #bbf7d0' } },
          error:   { style: { background: '#fef2f2', color: '#dc2626', border: '1px solid #fecaca' } },
        }}
      />
    </BrowserRouter>
  );
}
