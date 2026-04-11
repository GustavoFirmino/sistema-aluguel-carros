import { useEffect, useState } from 'react';
import { Plus, Eye, X, XCircle, FileText, ChevronRight, ScrollText } from 'lucide-react';
import toast from 'react-hot-toast';
import { pedidoService, clienteService, automovelService, agenteService, contratoService } from '../services/api';
import StatusBadge from '../components/StatusBadge';
import ConfirmModal from '../components/ConfirmModal';

const ALL_STATUS = ['PENDENTE','EM_ANALISE','APROVADO','REJEITADO','CANCELADO','CONCLUIDO'];

const NEXT_STATUS = {
  PENDENTE:   ['EM_ANALISE'],
  EM_ANALISE: ['APROVADO', 'REJEITADO'],
};

const STATUS_LABELS = {
  PENDENTE: 'Pendente', EM_ANALISE: 'Em Análise', APROVADO: 'Aprovado',
  REJEITADO: 'Rejeitado', CANCELADO: 'Cancelado', CONCLUIDO: 'Concluído',
};

// ─── Modal: Novo Pedido ────────────────────────────────────
function NovoPedidoModal({ onClose, onSave }) {
  const [form, setForm] = useState({ clienteId: '', automovelId: '', dataInicio: '', dataFim: '', observacao: '' });
  const [clientes, setClientes] = useState([]);
  const [automoveis, setAutomoveis] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    Promise.all([clienteService.listar(), automovelService.listar(true)])
      .then(([c, a]) => { setClientes(c.data); setAutomoveis(a.data); })
      .catch(() => toast.error('Erro ao carregar dados.'));
  }, []);

  const set = k => e => setForm(f => ({ ...f, [k]: e.target.value }));

  const submit = async e => {
    e.preventDefault();
    setLoading(true);
    try {
      await pedidoService.criar({
        clienteId:   parseInt(form.clienteId),
        automovelId: parseInt(form.automovelId),
        dataInicio:  form.dataInicio,
        dataFim:     form.dataFim,
        observacao:  form.observacao || undefined,
      });
      toast.success('Pedido criado!');
      onSave();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao criar pedido.');
    } finally {
      setLoading(false);
    }
  };

  const fmt = (val) => val != null
    ? new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(val)
    : '';

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal modal-lg" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Novo Pedido de Aluguel</h2>
          <button className="btn-icon" onClick={onClose}><X size={16} /></button>
        </div>
        <div className="modal-body">
          <form onSubmit={submit}>
            <div className="form-grid">
              <div className="form-group span-2">
                <label>Cliente *</label>
                <select className="form-control" value={form.clienteId} onChange={set('clienteId')} required>
                  <option value="">Selecione o cliente...</option>
                  {clientes.map(c => (
                    <option key={c.id} value={c.id}>{c.nome} — CPF: {c.cpf}</option>
                  ))}
                </select>
              </div>
              <div className="form-group span-2">
                <label>Automóvel disponível *</label>
                <select className="form-control" value={form.automovelId} onChange={set('automovelId')} required>
                  <option value="">Selecione o automóvel...</option>
                  {automoveis.map(a => (
                    <option key={a.id} value={a.id}>
                      {a.marca} {a.modelo} — {a.placa} ({a.ano}) · {fmt(a.valorDiaria)}/dia
                    </option>
                  ))}
                </select>
                {automoveis.length === 0 && (
                  <span style={{ fontSize: '.78rem', color: 'var(--warning)' }}>Nenhum automóvel disponível no momento.</span>
                )}
              </div>
              <div className="form-group">
                <label>Data de Início *</label>
                <input className="form-control" type="date" value={form.dataInicio} onChange={set('dataInicio')} required />
              </div>
              <div className="form-group">
                <label>Data de Fim *</label>
                <input className="form-control" type="date" value={form.dataFim} onChange={set('dataFim')} required />
              </div>
              <div className="form-group span-2">
                <label>Observação</label>
                <textarea className="form-control" rows={2} value={form.observacao} onChange={set('observacao')} placeholder="Informações adicionais..." />
              </div>
            </div>
            <div className="form-actions">
              <button type="button" className="btn btn-ghost" onClick={onClose}>Cancelar</button>
              <button type="submit" className="btn btn-primary" disabled={loading}>
                {loading ? 'Criando...' : 'Criar Pedido'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

// ─── Modal: Detalhes + Ações do Pedido ────────────────────
function PedidoDetailModal({ pedido, onClose, onRefresh }) {
  const [statusForm, setStatusForm] = useState({ status: '', observacao: '', agenteId: '' });
  const [agentesEmpresa, setAgentesEmpresa] = useState([]);
  const [loading, setLoading] = useState(false);
  const [cancelLoading, setCancelLoading] = useState(false);
  const [gerarLoading, setGerarLoading] = useState(false);
  const [confirmCancel, setConfirmCancel] = useState(false);

  const nexts = NEXT_STATUS[pedido.status] || [];

  useEffect(() => {
    if (pedido.status === 'PENDENTE') {
      agenteService.listar('EMPRESA')
        .then(r => setAgentesEmpresa(r.data))
        .catch(() => {});
    }
  }, [pedido.status]);

  const handleStatus = async e => {
    e.preventDefault();
    if (!statusForm.status) return;
    setLoading(true);
    try {
      const payload = {
        status: statusForm.status,
        observacao: statusForm.observacao || undefined,
      };
      if (statusForm.status === 'EM_ANALISE' && statusForm.agenteId) {
        payload.agenteId = parseInt(statusForm.agenteId);
      }
      await pedidoService.atualizarStatus(pedido.id, payload);
      toast.success('Status atualizado!');
      onRefresh();
      onClose();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao atualizar status.');
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = async () => {
    setCancelLoading(true);
    try {
      await pedidoService.cancelar(pedido.id);
      toast.success('Pedido cancelado.');
      setConfirmCancel(false);
      onRefresh();
      onClose();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao cancelar.');
    } finally {
      setCancelLoading(false);
    }
  };

  const handleGerarContrato = async () => {
    setGerarLoading(true);
    try {
      await contratoService.gerar(pedido.id);
      toast.success('Contrato gerado! O pedido foi concluído.');
      onRefresh();
      onClose();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao gerar contrato.');
    } finally {
      setGerarLoading(false);
    }
  };

  const fmt = (d) => d ? new Date(d + 'T00:00:00').toLocaleDateString('pt-BR') : '—';
  const fmtDT = (d) => d ? new Date(d).toLocaleString('pt-BR') : '—';

  return (
    <>
      <div className="modal-backdrop" onClick={onClose}>
        <div className="modal modal-lg" onClick={e => e.stopPropagation()}>
          <div className="modal-header">
            <h2>Pedido #{pedido.id}</h2>
            <div style={{ display: 'flex', gap: '.5rem', alignItems: 'center' }}>
              <StatusBadge status={pedido.status} />
              <button className="btn-icon" onClick={onClose}><X size={16} /></button>
            </div>
          </div>
          <div className="modal-body">
            {/* Info grid */}
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '.75rem 1.5rem', marginBottom: '1.25rem' }}>
              {[
                ['Cliente', pedido.clienteNome],
                ['CPF', pedido.clienteCpf],
                ['Automóvel', `${pedido.automovelMarca} ${pedido.automovelModelo}`],
                ['Placa', pedido.automovelPlaca],
                ['Data Início', fmt(pedido.dataInicio)],
                ['Data Fim', fmt(pedido.dataFim)],
                ['Criado em', fmtDT(pedido.dataCriacao)],
                ['Atualizado em', fmtDT(pedido.dataAtualizacao)],
              ].map(([label, value]) => (
                <div key={label}>
                  <div style={{ fontSize: '.72rem', fontWeight: 600, color: 'var(--gray-400)', textTransform: 'uppercase', letterSpacing: '.05em', marginBottom: '.2rem' }}>{label}</div>
                  <div style={{ fontSize: '.9rem', color: 'var(--gray-800)', fontWeight: 500 }}>{value}</div>
                </div>
              ))}
              {pedido.observacao && (
                <div style={{ gridColumn: 'span 2' }}>
                  <div style={{ fontSize: '.72rem', fontWeight: 600, color: 'var(--gray-400)', textTransform: 'uppercase', letterSpacing: '.05em', marginBottom: '.2rem' }}>Observação</div>
                  <div style={{ fontSize: '.9rem', color: 'var(--gray-700)' }}>{pedido.observacao}</div>
                </div>
              )}
            </div>

            <hr style={{ border: 'none', borderTop: '1px solid var(--gray-100)', margin: '1rem 0' }} />

            {/* Gerar contrato para pedido APROVADO */}
            {pedido.status === 'APROVADO' && (
              <div style={{ marginBottom: '1rem', padding: '1rem', background: '#f0fdf4', borderRadius: 8, border: '1px solid #86efac' }}>
                <p style={{ fontWeight: 600, color: '#166534', marginBottom: '.5rem' }}>Pedido aprovado — pronto para gerar contrato</p>
                <button className="btn btn-primary" onClick={handleGerarContrato} disabled={gerarLoading}>
                  <ScrollText size={15} />
                  {gerarLoading ? 'Gerando...' : 'Gerar Contrato'}
                </button>
              </div>
            )}

            {/* Ações do agente */}
            {nexts.length > 0 && (
              <form onSubmit={handleStatus}>
                <p style={{ fontWeight: 600, fontSize: '.85rem', color: 'var(--gray-700)', marginBottom: '.75rem' }}>
                  Atualizar Status (Agente)
                </p>
                <div className="form-grid">
                  <div className="form-group">
                    <label>Novo status *</label>
                    <select className="form-control" value={statusForm.status}
                      onChange={e => setStatusForm(f => ({ ...f, status: e.target.value }))} required>
                      <option value="">Selecione...</option>
                      {nexts.map(s => <option key={s} value={s}>{STATUS_LABELS[s]}</option>)}
                    </select>
                  </div>
                  <div className="form-group">
                    <label>Observação</label>
                    <input className="form-control" value={statusForm.observacao}
                      onChange={e => setStatusForm(f => ({ ...f, observacao: e.target.value }))} placeholder="Opcional" />
                  </div>
                  {statusForm.status === 'EM_ANALISE' && (
                    <div className="form-group span-2">
                      <label>Agente responsável *</label>
                      <select className="form-control" required value={statusForm.agenteId}
                        onChange={e => setStatusForm(f => ({ ...f, agenteId: e.target.value }))}>
                        <option value="">Selecione o agente...</option>
                        {agentesEmpresa.map(a => (
                          <option key={a.id} value={a.id}>{a.nome} — {a.cnpj}</option>
                        ))}
                      </select>
                      {agentesEmpresa.length === 0 && (
                        <span style={{ fontSize: '.78rem', color: 'var(--warning)' }}>
                          Nenhuma empresa agente cadastrada. Cadastre em Agentes primeiro.
                        </span>
                      )}
                    </div>
                  )}
                </div>
                <div className="form-actions" style={{ borderTop: 'none', marginTop: '.5rem', paddingTop: 0 }}>
                  {pedido.status === 'PENDENTE' && (
                    <button type="button" className="btn btn-danger" onClick={() => setConfirmCancel(true)}>
                      <XCircle size={15} /> Cancelar Pedido
                    </button>
                  )}
                  <button type="submit" className="btn btn-primary" disabled={loading}>
                    {loading ? 'Atualizando...' : <><ChevronRight size={15} /> Atualizar Status</>}
                  </button>
                </div>
              </form>
            )}

            {/* Status terminal */}
            {nexts.length === 0 && pedido.status === 'PENDENTE' && (
              <div className="form-actions" style={{ borderTop: 'none' }}>
                <button className="btn btn-danger" onClick={() => setConfirmCancel(true)}>
                  <XCircle size={15} /> Cancelar Pedido
                </button>
              </div>
            )}

            {nexts.length === 0 && !['PENDENTE', 'APROVADO'].includes(pedido.status) && (
              <div className="alert alert-info">
                Este pedido está em um estado final e não pode mais ser alterado.
              </div>
            )}
          </div>
        </div>
      </div>

      {confirmCancel && (
        <ConfirmModal
          title="Cancelar Pedido"
          message={`Tem certeza que deseja cancelar o pedido #${pedido.id}? O automóvel voltará a ficar disponível.`}
          onConfirm={handleCancel}
          onCancel={() => setConfirmCancel(false)}
          loading={cancelLoading}
        />
      )}
    </>
  );
}

// ─── Página Principal ─────────────────────────────────────
export default function Pedidos() {
  const [pedidos, setPedidos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filtroStatus, setFiltroStatus] = useState('');
  const [novoPedido, setNovoPedido] = useState(false);
  const [detalhe, setDetalhe] = useState(null);

  const load = () => {
    setLoading(true);
    pedidoService.listar()
      .then(r => setPedidos(r.data))
      .catch(() => toast.error('Erro ao carregar pedidos.'))
      .finally(() => setLoading(false));
  };

  useEffect(load, []);

  const filtered = filtroStatus
    ? pedidos.filter(p => p.status === filtroStatus)
    : pedidos;

  const fmt = d => d ? new Date(d + 'T00:00:00').toLocaleDateString('pt-BR') : '—';

  return (
    <div className="page">
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '1.5rem' }}>
        <div>
          <h2 style={{ fontSize: '1.25rem', fontWeight: 700, color: 'var(--gray-900)' }}>Pedidos de Aluguel</h2>
          <p style={{ color: 'var(--gray-500)', fontSize: '.875rem' }}>Crie e acompanhe o status dos pedidos</p>
        </div>
        <button className="btn btn-primary" onClick={() => setNovoPedido(true)}>
          <Plus size={16} /> Novo Pedido
        </button>
      </div>

      <div className="panel">
        <div className="panel-header">
          <span className="panel-title">{filtered.length} pedido{filtered.length !== 1 ? 's' : ''}</span>
          <select
            className="form-control"
            style={{ width: 'auto', minWidth: 160 }}
            value={filtroStatus}
            onChange={e => setFiltroStatus(e.target.value)}
          >
            <option value="">Todos os status</option>
            {ALL_STATUS.map(s => <option key={s} value={s}>{STATUS_LABELS[s]}</option>)}
          </select>
        </div>

        {loading ? (
          <div className="loading-center"><div className="spinner" /></div>
        ) : filtered.length === 0 ? (
          <div className="empty-state">
            <FileText size={36} />
            <p>{filtroStatus ? 'Nenhum pedido com este status.' : 'Nenhum pedido registrado ainda.'}</p>
          </div>
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Cliente</th>
                  <th>Automóvel</th>
                  <th>Placa</th>
                  <th>Início</th>
                  <th>Fim</th>
                  <th>Status</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map(p => (
                  <tr key={p.id}>
                    <td style={{ color: 'var(--gray-400)', fontSize: '.8rem' }}>{p.id}</td>
                    <td style={{ fontWeight: 500 }}>{p.clienteNome}</td>
                    <td>{p.automovelMarca} {p.automovelModelo}</td>
                    <td>
                      <span style={{ fontFamily: 'monospace', background: 'var(--gray-100)', padding: '.1rem .4rem', borderRadius: 4, fontSize: '.8rem' }}>
                        {p.automovelPlaca}
                      </span>
                    </td>
                    <td>{fmt(p.dataInicio)}</td>
                    <td>{fmt(p.dataFim)}</td>
                    <td><StatusBadge status={p.status} /></td>
                    <td>
                      <button className="btn-icon" title="Ver detalhes" onClick={() => setDetalhe(p)}>
                        <Eye size={15} />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {novoPedido && (
        <NovoPedidoModal
          onClose={() => setNovoPedido(false)}
          onSave={() => { setNovoPedido(false); load(); }}
        />
      )}

      {detalhe && (
        <PedidoDetailModal
          pedido={detalhe}
          onClose={() => setDetalhe(null)}
          onRefresh={load}
        />
      )}
    </div>
  );
}
