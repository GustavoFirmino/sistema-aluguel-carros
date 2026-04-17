import { NavLink } from 'react-router-dom';
import { Car, Users, FileText, LayoutDashboard, Building2, ScrollText, Landmark, ClipboardCheck } from 'lucide-react';

export default function Sidebar({ usuario }) {
  const role = usuario?.role;
  const isAdmin  = role === 'admin' || role === 'agente';
  const isBanco  = role === 'banco';
  const isEmpresa = role === 'empresa';

  const link = ({ isActive }) => `nav-link${isActive ? ' active' : ''}`;

  return (
    <aside className="sidebar">
      <div className="sidebar-brand">
        <div className="sidebar-brand-icon">
          <Car size={20} color="#fff" />
        </div>
        <h1>Roda Mundo</h1>
        <span>{isBanco ? 'Portal Banco' : isEmpresa ? 'Portal Empresa' : 'Painel Admin'}</span>
      </div>

      <nav className="sidebar-nav">

        {/* Admin / Agente */}
        {isAdmin && (<>
          <span className="nav-section-label">Principal</span>
          <NavLink to="/" end className={link}><LayoutDashboard size={17} /> Dashboard</NavLink>

          <span className="nav-section-label">Cadastros</span>
          <NavLink to="/clientes"   className={link}><Users      size={17} /> Clientes</NavLink>
          <NavLink to="/automoveis" className={link}><Car        size={17} /> Automóveis</NavLink>
          {role === 'admin' && (
            <NavLink to="/agentes"  className={link}><Building2  size={17} /> Agentes</NavLink>
          )}

          <span className="nav-section-label">Operações</span>
          <NavLink to="/pedidos"   className={link}><FileText   size={17} /> Pedidos</NavLink>
          <NavLink to="/contratos" className={link}><ScrollText size={17} /> Contratos</NavLink>
        </>)}

        {/* Banco */}
        {isBanco && (<>
          <span className="nav-section-label">Análise de Crédito</span>
          <NavLink to="/banco/pedidos" className={link}><Landmark size={17} /> Pedidos Pendentes</NavLink>
          <NavLink to="/automoveis"    className={link}><Car      size={17} /> Frota</NavLink>
        </>)}

        {/* Empresa */}
        {isEmpresa && (<>
          <span className="nav-section-label">Contratos</span>
          <NavLink to="/empresa/pedidos" className={link}><ClipboardCheck size={17} /> Pedidos Aprovados</NavLink>
          <NavLink to="/contratos"       className={link}><ScrollText     size={17} /> Contratos</NavLink>
          <NavLink to="/automoveis"      className={link}><Car            size={17} /> Frota</NavLink>
        </>)}

      </nav>

      <div className="sidebar-footer">
        PUC Minas · Lab. Dev. Software
      </div>
    </aside>
  );
}
