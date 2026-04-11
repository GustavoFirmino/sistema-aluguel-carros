const STATUS_MAP = {
  PENDENTE:   { label: 'Pendente',    cls: 'badge-yellow' },
  EM_ANALISE: { label: 'Em Análise',  cls: 'badge-blue'   },
  APROVADO:   { label: 'Aprovado',    cls: 'badge-green'  },
  REJEITADO:  { label: 'Rejeitado',   cls: 'badge-red'    },
  CANCELADO:  { label: 'Cancelado',   cls: 'badge-gray'   },
  CONCLUIDO:  { label: 'Concluído',   cls: 'badge-purple' },
};

export default function StatusBadge({ status }) {
  const { label, cls } = STATUS_MAP[status] || { label: status, cls: 'badge-gray' };
  return <span className={`badge ${cls}`}>{label}</span>;
}
