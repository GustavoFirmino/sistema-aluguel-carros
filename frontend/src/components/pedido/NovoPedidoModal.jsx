import { useEffect, useState } from 'react';
import { X, Car } from 'lucide-react';
import toast from 'react-hot-toast';
import { pedidoService, clienteService, automovelService } from '../../services/api';
import { useAuth } from '../../hooks/useAuth';

const fmt = (v) => v != null
  ? new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(v)
  : '';

/**
 * automovelPreSelecionado — opcional. Se fornecido (vindo da HomePage),
 * o carro já vem fixado e o cliente é determinado automaticamente pelo usuário logado.
 */
export default function NovoPedidoModal({ onClose, onSave, automovelPreSelecionado }) {
  const user = useAuth();
  const isClienteRole = user?.role === 'cliente';

  const [form, setForm] = useState({
    clienteId:   user?.clienteId ? String(user.clienteId) : '',
    automovelId: automovelPreSelecionado ? String(automovelPreSelecionado.id) : '',
    dataInicio:  '',
    dataFim:     '',
    observacao:  '',
  });
  const [clientes, setClientes]   = useState([]);
  const [automoveis, setAutomoveis] = useState([]);
  const [loading, setLoading]     = useState(false);

  useEffect(() => {
    const reqs = [];
    // Admin/agente veem lista de clientes; cliente vê só os próprios dados
    if (!isClienteRole) reqs.push(clienteService.listar());
    else reqs.push(Promise.resolve(null));

    // Se não tem carro pré-selecionado, carrega os disponíveis
    if (!automovelPreSelecionado) reqs.push(automovelService.listar(true));
    else reqs.push(Promise.resolve(null));

    Promise.all(reqs).then(([c, a]) => {
      if (c) setClientes(c.data.items ?? (Array.isArray(c.data) ? c.data : []));
      if (a) setAutomoveis(a.data.items ?? (Array.isArray(a.data) ? a.data : []));
    }).catch(() => toast.error('Erro ao carregar dados.'));
  }, []);

  const set = k => e => setForm(f => ({ ...f, [k]: e.target.value }));

  const submit = async e => {
    e.preventDefault();
    if (!form.clienteId) { toast.error('Selecione um cliente.'); return; }
    setLoading(true);
    try {
      await pedidoService.criar({
        clienteId:   parseInt(form.clienteId),
        automovelId: parseInt(form.automovelId),
        dataInicio:  form.dataInicio,
        dataFim:     form.dataFim,
        observacao:  form.observacao || undefined,
      });
      toast.success('Pedido criado com sucesso!');
      onSave();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao criar pedido.');
    } finally {
      setLoading(false);
    }
  };

  const today = new Date().toISOString().split('T')[0];

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal modal-lg" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Novo Pedido de Aluguel</h2>
          <button className="btn-icon" onClick={onClose}><X size={16} /></button>
        </div>
        <div className="modal-body">

          {/* Card do carro pré-selecionado */}
          {automovelPreSelecionado && (
            <div style={{
              display: 'flex', alignItems: 'center', gap: '1rem',
              padding: '.85rem 1rem', borderRadius: 10, marginBottom: '1.25rem',
              background: '#eff6ff', border: '1px solid #bfdbfe',
            }}>
              <div style={{ width: 44, height: 44, borderRadius: 8, background: '#dbeafe', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <Car size={22} color="#2563eb" />
              </div>
              <div>
                <div style={{ fontWeight: 700, color: '#1e3a8a' }}>
                  {automovelPreSelecionado.marca} {automovelPreSelecionado.modelo}
                </div>
                <div style={{ fontSize: '.8rem', color: '#2563eb' }}>
                  {automovelPreSelecionado.placa} · {automovelPreSelecionado.ano} · {fmt(automovelPreSelecionado.valorDiaria)}/dia
                </div>
              </div>
            </div>
          )}

          <form onSubmit={submit}>
            <div className="form-grid">

              {/* Cliente — só admin/agente selecionam; cliente role não vê o campo */}
              {!isClienteRole && (
                <div className="form-group span-2">
                  <label>Cliente *</label>
                  <select className="form-control" value={form.clienteId} onChange={set('clienteId')} required>
                    <option value="">Selecione o cliente...</option>
                    {clientes.map(c => (
                      <option key={c.id} value={c.id}>{c.nome} — CPF: {c.cpf}</option>
                    ))}
                  </select>
                </div>
              )}

              {/* Automóvel — só exibe select se não veio pré-selecionado */}
              {!automovelPreSelecionado && (
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
                    <span style={{ fontSize: '.78rem', color: 'var(--warning)' }}>
                      Nenhum automóvel disponível no momento.
                    </span>
                  )}
                </div>
              )}

              <div className="form-group">
                <label>Data de Início *</label>
                <input className="form-control" type="date" min={today}
                  value={form.dataInicio} onChange={set('dataInicio')} required />
              </div>
              <div className="form-group">
                <label>Data de Fim *</label>
                <input className="form-control" type="date" min={form.dataInicio || today}
                  value={form.dataFim} onChange={set('dataFim')} required />
              </div>
              <div className="form-group span-2">
                <label>Observação</label>
                <textarea className="form-control" rows={2} value={form.observacao}
                  onChange={set('observacao')} placeholder="Informações adicionais..." maxLength={500} />
              </div>
            </div>

            {/* Aviso se cliente logado sem clienteId vinculado */}
            {isClienteRole && !form.clienteId && (
              <div style={{ padding: '.65rem .9rem', borderRadius: 7, background: '#fffbeb', border: '1px solid #fde68a', fontSize: '.82rem', color: '#92400e', marginBottom: '.75rem' }}>
                Seu cadastro de cliente não está vinculado à conta. Contacte o administrador.
              </div>
            )}

            <div className="form-actions">
              <button type="button" className="btn btn-ghost" onClick={onClose}>Cancelar</button>
              <button type="submit" className="btn btn-primary"
                disabled={loading || (isClienteRole && !form.clienteId)}>
                {loading ? 'Criando...' : 'Criar Pedido'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
