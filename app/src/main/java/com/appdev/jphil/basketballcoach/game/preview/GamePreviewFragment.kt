package com.appdev.jphil.basketballcoach.game.preview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.databinding.FragmentGamePreviewBinding
import com.appdev.jphil.basketballcoach.game.GameViewModel
import com.appdev.jphil.basketballcoach.game.PlayerOverviewDialogFragment
import com.appdev.jphil.basketballcoach.main.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class GamePreviewFragment : Fragment() {

    @Inject
    lateinit var vmFactory: ViewModelFactory
    lateinit var viewModel: GameViewModel

    private val args: GamePreviewFragmentArgs by navArgs()
    private lateinit var binding: FragmentGamePreviewBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        viewModel = ViewModelProviders.of(requireParentFragment(), vmFactory).get(GameViewModel::class.java)
        viewModel.gameId = args.gameId
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGamePreviewBinding.inflate(inflater, container, false).apply {
            homeTeamName.text = args.homeTeamName
            awayTeamName.text = args.awayTeamName
            startGame.setOnClickListener {
                findNavController().navigate(GamePreviewFragmentDirections.actionGamePreviewFragmentToGameFragment(
                    args.gameId,
                    args.homeTeamName,
                    args.awayTeamName,
                    args.isUserHomeTeam
                ))
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runBlocking {
            val game = viewModel.getGame()
            binding.apply {
                homeTeamRating.text =
                    resources.getString(R.string.rating_colon, game.homeTeam.teamRating)
                awayTeamRating.text =
                    resources.getString(R.string.rating_colon, game.awayTeam.teamRating)
                recyclerView.apply {
                    layoutManager = GridLayoutManager(context, 2)
                    adapter = GamePreviewAdapter(resources) {
                        val dialog = PlayerOverviewDialogFragment()
                        dialog.player = it
                        fragmentManager?.let { fm ->
                            dialog.show(fm, "tag")
                        }
                    }.apply { setPlayers(game.homeTeam, game.awayTeam) }
                }
            }
        }
    }
}