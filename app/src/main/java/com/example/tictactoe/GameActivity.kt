package com.example.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.tictactoe.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityGameBinding

    private var gameModel: GameModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btn0.setOnClickListener(this)
        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(this)
        binding.btn3.setOnClickListener(this)
        binding.btn4.setOnClickListener(this)
        binding.btn5.setOnClickListener(this)
        binding.btn6.setOnClickListener(this)
        binding.btn7.setOnClickListener(this)
        binding.btn8.setOnClickListener(this)

        binding.startGameBtn.setOnClickListener {
            startGame()
        }

        GameData.gameModel.observe(this){
            gameModel = it
            setUI()
        }

    }

    private fun setUI(){
        gameModel?.apply {
            binding.btn0.text = filledPos[0]
            binding.btn1.text = filledPos[1]
            binding.btn2.text = filledPos[2]
            binding.btn3.text = filledPos[3]
            binding.btn4.text = filledPos[4]
            binding.btn5.text = filledPos[5]
            binding.btn6.text = filledPos[6]
            binding.btn7.text = filledPos[7]
            binding.btn8.text = filledPos[8]

            binding.gameStatusText.text =
                when(gameStatus){
                    GameStatus.CREATED ->{
                        "Game ID: $gameID"
                    }
                    GameStatus.JOINED ->{
                        "Click on start game"
                    }
                    GameStatus.IN_PROGRESS ->{
                        "$currentPlayer turn"
                    }
                    GameStatus.FINISHED ->{
                        if(winner.isNotEmpty()) "$winner won"
                        else "DRAW"
                    }
                }
        }
    }

    private fun startGame(){
        gameModel?.apply {
            updateGameData(
                GameModel(
                    gameID = gameID,
                    gameStatus = GameStatus.IN_PROGRESS
                )
            )
        }
    }

    private fun updateGameData(model: GameModel){
        GameData.saveGameModel(model)
    }

    override fun onClick(v: View?) {
        gameModel?.apply {
            if(gameStatus != GameStatus.IN_PROGRESS){
                Toast.makeText(applicationContext, "Game not started", Toast.LENGTH_SHORT).show()
                return
            }

            val clickPosition = (v?.tag as String).toInt()
            if(filledPos[clickPosition].isEmpty()){
                filledPos[clickPosition] = currentPlayer
                currentPlayer = if(currentPlayer == "X") "O" else "X"
                updateGameData(this)
            }
        }
    }
}