// [Template no Kotlin Playground](https://pl.kotl.in/WcteahpyN)

/**
* Lançada para indicar que um [nome] em branco e portanto inválido foi passado
*/
class NomeInvalidoException(val mensagem: String) : Exception(mensagem)

/**
* Lançada para indicar que uma [duracao] inválida, menor ou igual a zero, foi atribuida a [ConteudoEducacional]
*/
class DuracaoConteudoEducacionalInvalidoException(val mensagem: String) : Exception(mensagem)

/**
* Lançada ao tentar [matricular] um [Usuario] que já está inscrito na [Formacao]
*/
class MatriculaInvalidaException(val mensagem: String) : Exception(mensagem)

/**
* Validador com métodos para garantir uma inicialização consistente das classes:
* [Usuario], [ConteudoEducacional] e [Formacao]. Caso existam entradas inválidas os
* métodos lançam exceções; 
*/
class Validacoes {
    companion object {
        @Throws(NomeInvalidoException::class)
		fun validarNome(nome: String)  {
  	 		if(nome.isBlank()) throw NomeInvalidoException("O nome do usuário não pode estar vazio")
		}

		@Throws(DuracaoConteudoEducacionalInvalidoException::class)
		fun validarDuracaoConteudoEducacional(duracao: Int)  {
   	 		if(duracao <= 0) 
    			throw DuracaoConteudoEducacionalInvalidoException(
       	   	 	 "A duração do conteúdo educacional deve ser maior que zero"
     	  	 )
		}

		@Throws(MatriculaInvalidaException::class)
		fun validarMatriculaEmFormacao(vararg usuarios: Usuario, inscritosFormacao: Set<Usuario>) {
    		usuarios.forEach {
            	if(inscritosFormacao.contains(it)) throw MatriculaInvalidaException("Usuário ${it.nome} já está cadastrado")
        	}
		}
    }
}

/**
* Representa os níveis de dificuldade possíveis de uma [Formacao]
*/
enum class Nivel { BASICO, INTERMEDIARIO, AVANCADO }

/**
* Modelo de dados para representar um Usuário. O [nome] não pode ser nulo ou estar em branco
*/
data class Usuario(val nome: String) {
    init {
        Validacoes.validarNome(nome)
    }
}

/**
* Modelo de dados para representar um Conteúdo Educacional. A [duracao] do [ConteudoEducacional] deve 
* ser informada em minutos e ser maior do que zero. O [nome] não deve ser nulo ou estar em branco 
*/
data class ConteudoEducacional(val nome: String, val duracao: Int = 60) {
    init {
        Validacoes.validarNome(nome)
        Validacoes.validarDuracaoConteudoEducacional(duracao)
    }
} 

/**
* Abstração de um Formação. O [nome] não deve ser nulo ou estar em branco. Ao tentar [matricular] um aluno 
* já inscrito lança uma [MatriculaInvalidaException]
*/
class Formacao(
    val nome: String, 
    val nivel: Nivel, 
    val conteudosEducacionais: List<ConteudoEducacional>
) {
    init {
       Validacoes.validarNome(nome)
    }
    val inscritos = mutableSetOf<Usuario>()
    
    fun matricular(vararg usuarios: Usuario) {
        Validacoes.validarMatriculaEmFormacao(usuarios = *usuarios, inscritosFormacao = inscritos.toSet())
        inscritos.addAll(usuarios)
    }
}

fun main() {
    try {
        Usuario("")
    } catch(e: Exception) {
        println("Exceção de nome de usuário inválido capturada")
    }
    
    try {
        Formacao(nome = "  ", Nivel.BASICO, listOf(ConteudoEducacional(nome = "Introdução a linguagem Kotlin")))
    } catch(e: Exception) {
        println("Exceção de nome de Formação inválido capturada")
    }
    
    try {
        ConteudoEducacional(nome = "     ")
    } catch(e: Exception) {
        println("Exceção de nome de conteúdo educacional inválido capturada")
    }
    
    try {
        ConteudoEducacional(nome = "Kotlin", duracao = 0)
    } catch(e: Exception) {
        println("Exceção de duração de conteúdo educacional inválida capturada")
    }
   
    val conteudoEducacional = ConteudoEducacional(nome = "Kotlin")
    println(
        """A duração padrão do conteúdo educacional ${conteudoEducacional.nome} 
        deveria ser 60 min e é igual a ${conteudoEducacional.duracao} min"""
    )
     
    val conteudosEducacionais = listOf(
        ConteudoEducacional(nome = "Introdução a linguagem Kotlin"),
        ConteudoEducacional(nome = "Programação Orientada a Objetos", duracao = 240),
        ConteudoEducacional(nome = "Apresentação do Android Studio e Jetpack compose", duracao = 120)
    )
    
    val desenvolvimentoAndroid = Formacao(
        nome = "Desenvolvimento Android com Kotlin e Jetpack Compose", 
        nivel = Nivel.INTERMEDIARIO,
        conteudosEducacionais = conteudosEducacionais
    )
    
    val usuario1 = Usuario("Hugo Lacerda")
    val usuario2 = Usuario("Ana Maia")
  	val usuario3 = Usuario("Emilia Lins")
    
    desenvolvimentoAndroid.matricular(usuario1, usuario2)
    try{
    	desenvolvimentoAndroid.matricular(usuario2, usuario3)
    } catch(e: Exception) {
        println("Exceção de matrícula inválida por aluno repetido capturada")
    }
    desenvolvimentoAndroid.matricular(usuario3)
    println(
        """Os usuários inscritos na formação ${desenvolvimentoAndroid.nome} são 
        ${desenvolvimentoAndroid.inscritos.joinToString()}"""
    )
	println("""Os conteúdos educacionais da formação são: 
            ${desenvolvimentoAndroid.conteudosEducacionais}"""
           )
}