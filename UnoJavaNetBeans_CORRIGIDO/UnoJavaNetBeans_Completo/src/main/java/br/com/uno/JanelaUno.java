package br.com.uno;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class JanelaUno extends JFrame {
    private final JogoUno jogo;
    private final JLabel textoCpu = new JLabel("", SwingConstants.CENTER);
    private final JLabel textoMesa = new JLabel("", SwingConstants.CENTER);
    private final JLabel textoMensagem = new JLabel("", SwingConstants.CENTER);
    private final JLabel textoSelecao = new JLabel("", SwingConstants.CENTER);
    private final JPanel painelCartas = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 12));
    private final JButton botaoComprar = new JButton("COMPRAR (ESPAÇO)");
    private final JButton botaoNovoJogo = new JButton("NOVO JOGO");
    private int cartaSelecionada = 0;
    private boolean dialogoDeFimExibido;

    public JanelaUno() {
        jogo = new JogoUno();
        montarInterface();
        atualizarTela();
        setVisible(true);
    }

    private void montarInterface() {
        setTitle("UNO - Você contra a CPU");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(940, 680);
        setMinimumSize(new Dimension(780, 580));
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(247, 241, 250));
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("UNO", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 42));
        titulo.setForeground(new Color(190, 45, 65));

        textoCpu.setFont(new Font("SansSerif", Font.BOLD, 17));
        JPanel topo = new JPanel(new GridLayout(2, 1));
        topo.setOpaque(false);
        topo.add(titulo);
        topo.add(textoCpu);
        topo.setBorder(BorderFactory.createEmptyBorder(10, 8, 8, 8));
        add(topo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(3, 1, 5, 5));
        centro.setOpaque(false);

        JLabel legendaMesa = new JLabel("CARTA NA MESA", SwingConstants.CENTER);
        legendaMesa.setFont(new Font("SansSerif", Font.BOLD, 16));
        textoMesa.setFont(new Font("SansSerif", Font.BOLD, 28));
        textoMensagem.setFont(new Font("SansSerif", Font.BOLD, 16));
        textoMensagem.setForeground(new Color(95, 60, 115));
        textoSelecao.setFont(new Font("SansSerif", Font.PLAIN, 14));

        centro.add(legendaMesa);
        centro.add(textoMesa);
        centro.add(textoMensagem);
        add(centro, BorderLayout.CENTER);

        painelCartas.setOpaque(false);
        JScrollPane rolagem = new JScrollPane(painelCartas);
        rolagem.setBorder(BorderFactory.createTitledBorder("SUAS CARTAS - clique ou use as setas e ENTER"));
        rolagem.setPreferredSize(new Dimension(900, 190));

        JPanel inferior = new JPanel(new BorderLayout(5, 5));
        inferior.setOpaque(false);
        inferior.add(rolagem, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout());
        botoes.setOpaque(false);
        botaoComprar.addActionListener(e -> comprarCarta());
        botaoNovoJogo.addActionListener(e -> {
            jogo.novoJogo();
            cartaSelecionada = 0;
            dialogoDeFimExibido = false;
            atualizarTela();
        });
        botoes.add(botaoComprar);
        botoes.add(botaoNovoJogo);
        inferior.add(textoSelecao, BorderLayout.NORTH);
        inferior.add(botoes, BorderLayout.SOUTH);
        add(inferior, BorderLayout.SOUTH);

        configurarTeclado();
    }

    private void configurarTeclado() {
        JComponent raiz = getRootPane();
        raiz.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("RIGHT"), "proximaCarta");
        raiz.getActionMap().put("proximaCarta", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moverSelecao(1);
            }
        });

        raiz.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("LEFT"), "cartaAnterior");
        raiz.getActionMap().put("cartaAnterior", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moverSelecao(-1);
            }
        });

        raiz.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ENTER"), "jogarSelecionada");
        raiz.getActionMap().put("jogarSelecionada", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jogarCartaSelecionada();
            }
        });

        raiz.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("SPACE"), "comprarCarta");
        raiz.getActionMap().put("comprarCarta", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comprarCarta();
            }
        });
    }

    private void moverSelecao(int movimento) {
        if (!jogo.ehVezDaPessoa() || jogo.getPessoa().quantidadeCartas() == 0) {
            return;
        }

        int quantidade = jogo.getPessoa().quantidadeCartas();
        cartaSelecionada = (cartaSelecionada + movimento + quantidade) % quantidade;
        atualizarTela();
    }

    private void jogarCartaSelecionada() {
        jogarCarta(cartaSelecionada);
    }

    private void jogarCarta(int posicao) {
        if (!jogo.ehVezDaPessoa() || posicao < 0
                || posicao >= jogo.getPessoa().quantidadeCartas()) {
            return;
        }

        Carta carta = jogo.getPessoa().getMao().get(posicao);
        if (!jogo.podeJogar(carta)) {
            jogo.jogarCartaDaPessoa(posicao, null);
            atualizarTela();
            return;
        }

        Carta.Cor corEscolhida = null;
        if (carta.getTipo() == Carta.Tipo.CORINGA) {
            corEscolhida = perguntarCor();
        }

        jogo.jogarCartaDaPessoa(posicao, corEscolhida);
        cartaSelecionada = Math.max(0, Math.min(posicao, jogo.getPessoa().quantidadeCartas() - 1));
        atualizarTela();
        executarCpuSeNecessario();
    }

    private Carta.Cor perguntarCor() {
        Object[] opcoes = {"Vermelho", "Azul", "Verde", "Amarelo"};
        int escolha = JOptionPane.showOptionDialog(this, "Escolha a cor do coringa:",
                "Escolher cor", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opcoes, opcoes[0]);

        switch (escolha) {
            case 1: return Carta.Cor.AZUL;
            case 2: return Carta.Cor.VERDE;
            case 3: return Carta.Cor.AMARELO;
            default: return Carta.Cor.VERMELHO;
        }
    }

    private void comprarCarta() {
        if (!jogo.ehVezDaPessoa()) {
            return;
        }
        jogo.comprarCartaParaPessoa();
        cartaSelecionada = Math.max(0, jogo.getPessoa().quantidadeCartas() - 1);
        atualizarTela();
        executarCpuSeNecessario();
    }

    private void executarCpuSeNecessario() {
        if (!jogo.terminou() && !jogo.ehVezDaPessoa()) {
            Timer temporizador = new Timer(650, evento -> {
                ((Timer) evento.getSource()).stop();
                jogo.jogarVezDaCpu();
                cartaSelecionada = 0;
                atualizarTela();
                executarCpuSeNecessario();
            });
            temporizador.setRepeats(false);
            temporizador.start();
        }
    }

    private void atualizarTela() {
        textoCpu.setText("CPU: " + jogo.getComputador().quantidadeCartas()
                + " cartas     |     Baralho: " + jogo.getQuantidadeNoBaralho()
                + " cartas     |     Suas cartas: " + jogo.getPessoa().quantidadeCartas());

        Carta cartaMesa = jogo.getCartaDaMesa();
        textoMesa.setText("<html><div style='text-align:center'>"
                + "<span style='color:#555'>Cor atual: " + nomeCor(jogo.getCorAtual()) + "</span><br>"
                + "<span style='font-size:28pt'>" + cartaMesa.getNome() + "</span>"
                + "</div></html>");

        textoMensagem.setText(jogo.getMensagem());
        painelCartas.removeAll();

        int indice = 0;
        for (Carta carta : jogo.getPessoa().getMao()) {
            final int posicao = indice;
            JButton botaoCarta = new JButton("<html><center><b>" + carta.getNome()
                    + "</b><br><small>" + carta.getCorEmPortugues() + "</small></center></html>");
            botaoCarta.setPreferredSize(new Dimension(88, 112));
            botaoCarta.setFont(new Font("SansSerif", Font.BOLD, 15));
            botaoCarta.setBackground(carta.getCorVisual());
            botaoCarta.setForeground(carta.getCor() == Carta.Cor.AMARELO ? Color.BLACK : Color.WHITE);
            botaoCarta.setFocusPainted(false);
            botaoCarta.setBorder(BorderFactory.createLineBorder(
                    posicao == cartaSelecionada ? Color.BLACK : Color.WHITE,
                    posicao == cartaSelecionada ? 4 : 1));
            botaoCarta.setEnabled(jogo.ehVezDaPessoa());
            botaoCarta.addActionListener(e -> jogarCarta(posicao));
            painelCartas.add(botaoCarta);
            indice++;
        }

        textoSelecao.setText("Carta selecionada: " + (jogo.getPessoa().quantidadeCartas() == 0
                ? "-" : (cartaSelecionada + 1)) + "   |   Setas: escolher   Enter: jogar   Espaço: comprar");

        botaoComprar.setEnabled(jogo.ehVezDaPessoa());
        if (jogo.terminou() && !dialogoDeFimExibido) {
            dialogoDeFimExibido = true;
            JOptionPane.showMessageDialog(this, jogo.getMensagem(), "Fim de jogo",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        painelCartas.revalidate();
        painelCartas.repaint();
    }

    private String nomeCor(Carta.Cor cor) {
        switch (cor) {
            case VERMELHO: return "Vermelho";
            case AZUL: return "Azul";
            case VERDE: return "Verde";
            case AMARELO: return "Amarelo";
            default: return "Coringa";
        }
    }
}
