package com.xxx.xxxmusic.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xxx.xxxmusic.databinding.FragmentMainBinding
import com.xxx.xxxmusic.databinding.ItemIamgeTxtBinding

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root = binding.root

        pageViewModel.list.observe(viewLifecycleOwner) {
            binding.rvContainer.apply {
                adapter = ViewAdapter(it)
                layoutManager = LinearLayoutManager(requireContext())
            }

        }
        return root
    }

    inner class ViewAdapter(val list: List<Int>) : RecyclerView.Adapter<ViewAdapter.Holder>() {
        private lateinit var binding: ItemIamgeTxtBinding

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            binding = ItemIamgeTxtBinding.inflate(layoutInflater)
            return Holder(binding.root)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            binding.ivContainer.setBackgroundResource(list[position])
        }

        override fun getItemCount(): Int {
            return list.size
        }
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}