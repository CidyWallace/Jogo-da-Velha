package org.example

import java.util.Scanner

fun main() {
    val sc = Scanner(System.`in`)
    val matriz = Array(3) { Array (3) {" "} }
    val player1 = "x"
    val player2 = "o"

    println("Escolha um modo de jogo: (m) 2 playes, (s) 1 player")
    val modo = sc.next()
    if(modo.lowercase() == "m"){
        multiplayer(matriz, sc, player1, player2)
    }else if(modo.lowercase() == "s"){
        PvsR(matriz, sc, player1, player2)
    }
    sc.close()
}

fun PvsR(matriz: Array<Array<String>>, sc: Scanner, player1: String, player2: String) {
    var turno = true
    var win = false
    while(!win){
        showGame(matriz)
        println()
        if(turno){
            println("Turno x\n")
            play(matriz, sc, player1)
            win = checkEndGame(matriz, player1)
            turno = false
        }else{
            println("Turno O\n")
            aiMoveMinimax(matriz, player2)
            win = checkEndGame(matriz, player2)
            turno = true
        }
    }
}

fun multiplayer(matriz: Array<Array<String>>, sc: Scanner, player1: String, player2: String) {
    var win = false
    var turno = true
    while(!win){
        showGame(matriz)
        println()
        if(turno){
            println("Turno X\n")
            play(matriz, sc, player1)
            win = checkEndGame(matriz, player1)
            turno = false
        }else{
            println("Turno O\n")
            play(matriz, sc, player2)
            win = checkEndGame(matriz, player2)
            turno = true
        }
    }
}

fun playRandom(matriz: Array<Array<String>>, player2: String) {
    var valid = false
    var linha: Int
    var coluna: Int
    do{
        linha = (0..2).random()
        coluna = (0..2).random()
        if(matriz[linha][coluna] == "-"){
            matriz[linha][coluna] = player2
            valid = true
        }
    }while (!valid)
}

fun play(matriz: Array<Array<String>>, sc: Scanner, player: String) {
    var valid = false
    do{
        println("Digite a linha:")
        val linha = sc.nextInt()
        println("Digite a coluna:")
        val coluna = sc.nextInt()

        if(matriz.count() > linha && matriz.count() > coluna && matriz[linha][coluna] == " "){
            matriz[linha][coluna] = player
            valid = true
        }else{
            println("Posição inválida ou já preenchida")
        }
    }while (!valid)
}

fun showGame(matriz: Array<Array<String>>) {
    println("   0  1  2")
    println("0 ${matriz[0].slice(0..2)}")
    println("1 ${matriz[1].slice(0..2)}")
    println("2 ${matriz[2].slice(0..2)}")
}

fun verifier(matriz: Array<Array<String>>, player: String): Boolean{
    for(i in 0..2) {
        if((0..2).all { matriz[i][it] == player }) return true
        if((0..2).all { matriz[it][i] == player }) return true
    }
    if ((0..2).all { matriz[it][it] == player }) return true
    if ((0..2).all { matriz[it][2 - it] == player }) return true

    return false
}

fun checkEndGame(matriz: Array<Array<String>>, player: String): Boolean{
    if(verifier(matriz, player)){
        showGame(matriz)
        println("$player Wins")
        return true
    }
    if(isBoardFull(matriz)){
        showGame(matriz)
        println("Empate!")
        return true
    }

    return false
}

fun isBoardFull(board: Array<Array<String>>): Boolean {
    return board.all { row -> row.all { it != " " } }
}

fun aiMoveMinimax(board: Array<Array<String>>, aiPlayer: String) {
    var bestScore = Int.MIN_VALUE
    var bestMove: Pair<Int, Int>? = null
    val humanPlayer = if (aiPlayer == "x") "o" else "x"

    for (i in 0..2) {
        for (j in 0..2) {
            if (board[i][j] == " ") {
                board[i][j] = aiPlayer
                val score = minimax(board, 0, false, aiPlayer, humanPlayer)
                board[i][j] = " "
                if (score > bestScore) {
                    bestScore = score
                    bestMove = i to j
                }
            }
        }
    }

    bestMove?.let { (i, j) ->
        println("IA (Minimax) jogou em $i $j")
        board[i][j] = aiPlayer
    }
}

fun minimax(
    board: Array<Array<String>>,
    depth: Int,
    isMaximizing: Boolean,
    aiPlayer: String,
    humanPlayer: String
): Int {
    if (verifier(board, aiPlayer)) return 10 - depth
    if (verifier(board, humanPlayer)) return depth - 10
    if (isBoardFull(board)) return 0

    if (isMaximizing) {
        var bestScore = Int.MIN_VALUE
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j] == " ") {
                    board[i][j] = aiPlayer
                    val score = minimax(board, depth + 1, false, aiPlayer, humanPlayer)
                    board[i][j] = " "
                    bestScore = maxOf(bestScore, score)
                }
            }
        }
        return bestScore
    } else {
        var bestScore = Int.MAX_VALUE
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j] == " ") {
                    board[i][j] = humanPlayer
                    val score = minimax(board, depth + 1, true, aiPlayer, humanPlayer)
                    board[i][j] = " "
                    bestScore = minOf(bestScore, score)
                }
            }
        }
        return bestScore
    }
}
