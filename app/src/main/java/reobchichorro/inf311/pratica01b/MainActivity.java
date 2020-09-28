package reobchichorro.inf311.pratica01b;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    double resultado;
    Button mais, menos, vezes, divisao;
    Button[] digitos = new Button[10];
    Button ponto, limpar, backspace, igual;

    String[] nome_digitos = {"zero", "um", "dois", "tres", "quatro", "cinco", "seis", "sete", "oito", "nove"};

    String visor;
    TextView entrada, resultado_output;

    public String formatar(double num)
    {
        if(num == (long) num)
            return String.format("%d",(long)num);
        else
            return String.format("%s",num);
    }

    public void visor_pop() {
        System.out.println(visor.substring(0, visor.length() - 1));
        if(visor != "")
            visor = visor.substring(0, visor.length() - 1);
    }

    // Retorna true se houver erro.
    private boolean converter_para_numero(ArrayList<Double> numeros, String str) {
        int numero_de_pontos = 0;
        for(int i=0; i<str.length(); i++) {
            if(str.charAt(i) == '.')
                numero_de_pontos++;
        }
        if(numero_de_pontos>1) return true;

        numeros.add(Double.valueOf(str));
        return false;
    }

    private void atribuicoes() {
        visor = "";

        entrada = (TextView) findViewById(R.id.entrada);
        resultado_output = (TextView) findViewById(R.id.resultadot);

        mais = (Button) findViewById(R.id.op_mais);
        menos = (Button) findViewById(R.id.op_menos);
        vezes = (Button) findViewById(R.id.op_vezes);
        divisao = (Button) findViewById(R.id.op_divisao);

        ponto = (Button) findViewById(R.id.ponto);
        limpar = (Button) findViewById(R.id.limpar);
        backspace = (Button) findViewById(R.id.backspace);
        igual = (Button) findViewById(R.id.igual);

        digitos[0] = (Button) findViewById(R.id.zero);
        digitos[1] = (Button) findViewById(R.id.um);
        digitos[2] = (Button) findViewById(R.id.dois);
        digitos[3] = (Button) findViewById(R.id.tres);
        digitos[4] = (Button) findViewById(R.id.quatro);
        digitos[5] = (Button) findViewById(R.id.cinco);
        digitos[6] = (Button) findViewById(R.id.seis);
        digitos[7] = (Button) findViewById(R.id.sete);
        digitos[8] = (Button) findViewById(R.id.oito);
        digitos[9] = (Button) findViewById(R.id.nove);
    }

    private void clickListeners() {

        for(int i=0; i<10; i++) {
            final int ii = i;
            digitos[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    visor+=Integer.toString(ii);
                    entrada.setText(visor);
                }
            });
        }

        mais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visor+="+";
                entrada.setText(visor);
            }
        });
        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visor+="-";
                entrada.setText(visor);
            }
        });
        vezes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visor+="*";
                entrada.setText(visor);
            }
        });
        divisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visor+="/";
                entrada.setText(visor);
            }
        });

        ponto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visor+=".";
                entrada.setText(visor);
            }
        });
        limpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visor="";
                entrada.setText(visor);
            }
        });
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visor_pop();
                entrada.setText(visor);
            }
        });
        igual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processar_visor(visor);
            }
        });

    }

    private void inserir_operacoes(String str, ArrayList<Integer> ops) {
        for(int i=0; i<str.length(); i++) {
            if(str.charAt(i) == '+')
                ops.add(1);
            else if(str.charAt(i) == '-')
                ops.add(2);
            else if(str.charAt(i) == '*')
                ops.add(3);
            else if(str.charAt(i) == '/')
                ops.add(4);
        }
    }

    private boolean operacao(double valor2, int op) {
        switch(op) {
            case 1:
                resultado += valor2;
                break;
            case 2:
                resultado -= valor2;
                break;
            case 3:
                resultado *= valor2;
                break;
            case 4:
                if(Math.abs(valor2) < 1e-15)
                    return false;
                else
                    resultado /= valor2;
                break;
        }
        return true;
    }

    private boolean calcular_expressao(ArrayList<Double> numeros, ArrayList<Integer> ops) {
        resultado = numeros.get(0);
        for(int i=0; i<ops.size(); i++) {
            if(!operacao(numeros.get(i+1), ops.get(i)))
                return false;
        }
        return true;
    }

    private void processar_visor(String s) {

        String[] expressoes = s.split("\\+|-|\\*|/");
        ArrayList<Double> numeros = new ArrayList<Double>();
        ArrayList<Integer> ops = new ArrayList<Integer>();

        if(s.trim().length()==0) {
            return;
        }

        for(String sub : expressoes) {
            if(sub == "") {
                resultado_output.setText("Erro: operação mal-formatada.");
                return;
            }
            if(converter_para_numero(numeros, sub)) {
                resultado_output.setText("Erro: operação mal-formatada.");
                return;
            }
        }

        inserir_operacoes(s, ops);

        if(ops.size() + 1 != numeros.size()) {
            resultado_output.setText("Erro: operação mal-formatada.");
            return;
        }

        resultado = 0.0;

        if(calcular_expressao(numeros, ops))
            resultado_output.setText("O resultado é "+formatar(resultado));
        else
            resultado_output.setText("Erro: divisão por 0 ou por número muito pequeno. Realize outra operação.");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        atribuicoes();
        clickListeners();
    }
}
