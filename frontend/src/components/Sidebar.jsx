import { NavLink } from 'react-router-dom';
import { Car, Users, FileText, LayoutDashboard, Building2, ScrollText } from 'lucide-react';

export default function Sidebar() {
  return (
    <aside className="sidebar">
      <div className="sidebar-brand">
        <div className="sidebar-brand-icon">
          <Car size={20} color="#fff" />
        </div>
        <h1>Aluguel de Carros</h1>
        <span>Sistema de Gestão</span>
      </div>

      <nav className="sidebar-nav">
        <span className="nav-section-label">Principal</span>

        <NavLink to="/" end className={({ isActive }) => `nav-link${isActive ? ' active' : ''}`}>
          <LayoutDashboard size={17} />
          Dashboard
        </NavLink>

        <span className="nav-section-label">Cadastros</span>

        <NavLink to="/clientes" className={({ isActive }) => `nav-link${isActive ? ' active' : ''}`}>
          <Users size={17} />
          Clientes
        </NavLink>

        <NavLink to="/automoveis" className={({ isActive }) => `nav-link${isActive ? ' active' : ''}`}>
          <Car size={17} />
          Automóveis
        </NavLink>

        <NavLink to="/agentes" className={({ isActive }) => `nav-link${isActive ? ' active' : ''}`}>
          <Building2 size={17} />
          Agentes
        </NavLink>

        <span className="nav-section-label">Operações</span>

        <NavLink to="/pedidos" className={({ isActive }) => `nav-link${isActive ? ' active' : ''}`}>
          <FileText size={17} />
          Pedidos
        </NavLink>

        <NavLink to="/contratos" className={({ isActive }) => `nav-link${isActive ? ' active' : ''}`}>
          <ScrollText size={17} />
          Contratos
        </NavLink>
      </nav>

      <div className="sidebar-footer">
        PUC Minas · Lab. Dev. Software
      </div>
    </aside>
  );
}
