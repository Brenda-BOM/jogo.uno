package br.com.uno;

import java.awt.Color;

public class Carta {
    public enum Cor {
        VERMELHO, AZUL, VERDE, AMARELO, CORINGA
    }

    public enum Tipo {
        NUMERO, MAIS_DOIS, BLOQUEIO, INVERTER, CORINGA
    }

    private final Cor cor;
    private final Tipo tipo;
    private final int numero;

    public Carta(Cor cor, int numero) {
        this.cor = cor;
        this.tipo = Tipo.NUMERO;
        this.numero = numero;
    }

    public Carta(Cor cor, Tipo tipo) {
        this.cor = cor;
        this.tipo = tipo;
        this.numero = -1;
    }

    public Cor getCor() {
        return cor;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public int getNumero() {
        return numero;
    }

    public String getNome() {
        switch (tipo) {
            case NUMERO: return String.valueOf(numero);
            case MAIS_DOIS: return "+2";
            case BLOQUEIO: return "BLOQUEIO";
            case INVERTER: return "INVERTER";
            case CORINGA: return "CORINGA";
            default: return "";
        }
    }

    public Color getCorVisual() {
        switch (cor) {
            case VERMELHO: return new Color(210, 55, 65);
            case AZUL: return new Color(45, 115, 205);
            case VERDE: return new Color(35, 145, 85);
            case AMARELO: return new Color(245, 190, 45);
            default: return new Color(65, 65, 70);
        }
    }

    public String getCorEmPortugues() {
        switch (cor) {
            case VERMELHO: return "Vermelho";
            case AZUL: return "Azul";
            case VERDE: return "Verde";
            case AMARELO: return "Amarelo";
            default: return "Coringa";
        }
    }

    @Override
    public String toString() {
        return getCorEmPortugues() + " " + getNome();
    }
}
