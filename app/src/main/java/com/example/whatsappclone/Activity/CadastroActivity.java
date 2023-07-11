package com.example.whatsappclone.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappclone.Config.ConfiguracaoFirebase;
import com.example.whatsappclone.Model.Usuario;
import com.example.whatsappclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {
    private TextInputEditText campoNome, campoEmail, campoSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.EditNome);
        campoEmail = findViewById(R.id.EditEmail);
        campoSenha = findViewById(R.id.EditSenha);
    }
    public  void cadastrarUsuario(Usuario usuario){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(),usuario.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    //usuario.setIdUsuario(idUsuario);
                    //usuario.salvar();
                    finish();
                    Toast.makeText(CadastroActivity.this,"Usuário criado com sucesso",Toast.LENGTH_LONG).show();
                }else{
                    String excecao;
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao ="Digite uma senha mais forte";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Por favor digitar email válido";
                    }catch (FirebaseAuthUserCollisionException e){
                        excecao = "Está conta já foi cadastrada";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this,excecao,Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void validarCadastroUsuario(View view) {
        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        if (!textoNome.isEmpty()) {
            if (!textoEmail.isEmpty()) {
                if (!textoSenha.isEmpty()) {
                    Usuario usuario = new Usuario();
                    usuario.setNome(textoNome);
                    usuario.setEmail(textoEmail);
                    usuario.setSenha(textoSenha);
                    cadastrarUsuario(usuario);
                } else {
                    Toast.makeText(CadastroActivity.this, "é obrigatório preencher nome", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(CadastroActivity.this, "é obrigatório preencher email", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(CadastroActivity.this, "é obrigatório preencher senha", Toast.LENGTH_LONG).show();
        }
    }
}

