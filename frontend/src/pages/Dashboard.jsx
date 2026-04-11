import { useEffect, useState } from 'react';
import { Users, Car, FileText, CheckCircle, Clock, XCircle, ScrollText, Building2 } from 'lucide-react';
import { clienteService, automovelService, pedidoService, contratoService, agenteService } from '../services/api';

function StatCard({ icon: Icon, label, value, iconBg, iconColor }) {
  return (
    <div className="stat-card">
      <div className="stat-icon" style={{ background: iconBg }}>
        <Icon size={18} color={iconColor} />
      </div>
      <div className="stat-value">{value ?? '—'}</div>
      <div className="stat-label">{label}</div>
    </div>
  );
}

function RecentPedidoRow({ pedido }) {
  const STATUS = {
    PENDENTE:   { label: 'Pendente',   cls: 'badge-yellow' },
    EM_ANALISE: { label: 'Em Análise', cls: 'badge-blue'   },
    APROVADO:   { label: 'Aprovado',   cls: 'badge-green'  },
    REJEITADO:  { label: 'Rejeitado',  cls: 'badge-red'    },
    CANCELADO:  { label: 'Cancelado',  cls: 'badge-gray'   },
    CONCLUIDO:  { label: 'Concluído',  cls: 'badge-purple' },
  };
  const s = STATUS[pedido.status] || { label: pedido.status, cls: 'badge-gray' };
  const fmt = d => d ? new Date(d + 'T00:00:00').toLocaleDateString('pt-BR') : '—';
  return (
    <tr>
      <td>#{pedido.id}</td>
      <td>{pedido.clienteNome}</td>
      <td>{pedido.automovelMarca} {pedido.automovelModelo}</td>
      <td>{fmt(pedido.dataInicio)}</td>
      <td><span className={`badge ${s.cls}`}>{s.label}</span></td>
    </tr>
  );
}

export default function Dashboard() {
  const [stats, setStats] = useState({
    clientes: null, totalAutos: null, disponiveis: null,
    pendentes: null, aprovados: null, cancelados: null,
    contratos: null, agentes: null,
  });
  const [pedidos, setPedidos] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      clienteService.listar(),
      automovelService.listar(),
      automovelService.listar(true),
      pedidoService.listar(),
      contratoService.listar(),
      agenteService.listar(),
    ]).then(([c, a, aDisp, p, ct, ag]) => {
      const todos = p.data;
      setStats({
        clientes:    c.data.length,
        totalAutos:  a.data.length,
        disponiveis: aDisp.data.length,
        pendentes:   todos.filter(x => x.status === 'PENDENTE').length,
        aprovados:   todos.filter(x => x.status === 'APROVADO').length,
        cancelados:  todos.filter(x => x.status === 'CANCELADO' || x.status === 'REJEITADO').length,
        contratos:   ct.data.length,
        agentes:     ag.data.length,
      });
      setPedidos(todos.slice(0, 8));
    }).finally(() => setLoading(false));
  }, []);

  return (
    <div className="page">
      <div style={{ marginBottom: '1.5rem' }}>
        <h2 style={{ fontSize: '1.25rem', fontWeight: 700, color: 'var(--gray-900)' }}>Dashboard</h2>
        <p style={{ color: 'var(--gray-500)', fontSize: '.875rem', marginTop: '.25rem' }}>
          Visão geral do sistema de aluguel de carros
        </p>
      </div>

      {loading ? (
        <div className="loading-center"><div className="spinner" /></div>
      ) : (
        <>
          <div className="stat-grid">
            <StatCard icon={Users}       label="Clientes Cadastrados" value={stats.clientes}    iconBg="#eff6ff" iconColor="var(--primary)" />
            <StatCard icon={Car}         label="Automóveis na Frota"  value={stats.totalAutos}  iconBg="#f0fdf4" iconColor="var(--success)" />
            <StatCard icon={Car}         label="Disponíveis"          value={stats.disponiveis} iconBg="#f0fdf4" iconColor="var(--success)" />
            <StatCard icon={Building2}   label="Agentes Cadastrados"  value={stats.agentes}     iconBg="#faf5ff" iconColor="#7c3aed" />
            <StatCard icon={Clock}       label="Pedidos Pendentes"    value={stats.pendentes}   iconBg="#fffbeb" iconColor="var(--warning)" />
            <StatCard icon={CheckCircle} label="Pedidos Aprovados"    value={stats.aprovados}   iconBg="#eff6ff" iconColor="var(--primary)" />
            <StatCard icon={XCircle}     label="Recusados/Cancelados" value={stats.cancelados}  iconBg="#fef2f2" iconColor="var(--danger)"  />
            <StatCard icon={ScrollText}  label="Contratos Gerados"    value={stats.contratos}   iconBg="#f0fdfa" iconColor="#0d9488" />
          </div>

          <div className="panel">
            <div className="panel-header">
              <span className="panel-title">Pedidos Recentes</span>
            </div>
            {pedidos.length === 0 ? (
              <div className="empty-state">
                <FileText size={32} />
                <p>Nenhum pedido registrado ainda.</p>
              </div>
            ) : (
              <div className="table-wrap">
                <table>
                  <thead>
                    <tr>
                      <th>#</th>
                      <th>Cliente</th>
                      <th>Automóvel</th>
                      <th>Data Início</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    {pedidos.map(p => <RecentPedidoRow key={p.id} pedido={p} />)}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </>
      )}
    </div>
  );
}
