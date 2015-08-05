# testWalMart
Teste do Walmart


Desafio

Desenvolver um sistema de entregas visando sempre o menor custo. Para popular sua base de dados o sistema precisa expor um Webservices que aceite o formato de malha logística (exemplo abaixo) e nesta mesma requisição o requisitante deverá informar um nome para este mapa. É importante que os mapas sejam persistidos para evitar que a cada novo deploy todas as informações desapareçam. O formato de malha logística é bastante simples, cada linha mostra uma rota: ponto de origem, ponto de destino e distância entre os pontos em quilômetros.

A B 10

B D 15

A C 20

C D 30

B E 50

D E 30

Um exemplo de entrada seria, origem A, destino D, autonomia 10, valor do litro 2,50; a resposta seria a rota A B D com custo de 6,25.


Apresentacao
============

	Aplicação de Rotas foi desenvolvida para calcular a rota de menor custo entre dois pontos em uma malha logistica conforme desafio proposto.
	Expoe dois endpoints para a persistencia da malha logistica e para o calculo da rota de menor custo. 

	Arquitetura:
	============
	O projeto foi desenvolvido usando a Plataforma JEE 8 - Java Enterprise Edition, empregando APIs 

    REST Services             JPA (backed by Hibernate)
    Spring core 4.0           Spring data 1.5.2
    Spring MVC 4.0            Hibernate 4.3.1
    Junit 4.8.1               Mockito 1.9.5
    Derby 10.4.2


Endpoint de persistencia: utilizado para persistir uma malha logistica.
Parmetros do header: Content-Type application/json

    http://localhost:8080/rotas/malhas
    {"nome":"rota 1", "pontoOrigem":"A","pontoDestino":"B", "distancia":10 }

Para buscar melhor rota logística:

    http://localhost:8080/rotas/trajetos?origem={origem}&destino={destino}&autonomia={autonomia}&valorLitro={valorLitro}


