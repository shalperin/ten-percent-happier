package com.blauhaus.android.redwood.app.todomvvm

import android.os.Bundle
import android.text.Layout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.blauhaus.android.redwood.app.R
import com.blauhaus.android.redwood.app.login.LoginViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_todo_mvvm.*
import kotlinx.android.synthetic.main.rv_item_todos.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel



/**
 * A simple [Fragment] subclass.
 */
class TodoMvvmFragment : Fragment() {
    private val loginViewModel by viewModel<LoginViewModel>()
    private val todoViewModel by viewModel<TodoViewModel>()
    val TAG = "TODO_FRAG"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo_mvvm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.authenticationState.observe(this, Observer {
            if (it == LoginViewModel.AuthenticationState.UNAUTHENTICATED) {
                findNavController().navigate(R.id.action_global_loginFragment)
            }
        })

        todoViewModel.getTodoLiveData("Y6uPXpaVVuAu03bAFGgT")
            .observe(this, Observer {
                Log.d(TAG, "porsche")
                if (it.data != null) {
                    Log.d(TAG, it.data.toString())
                } else {
                    Log.d(TAG, "EXCEPTION ", it.exception)
                }
            })

        val rvAdapter = TodoListAdapter()
        todo_rv.adapter = rvAdapter
        todo_rv.layoutManager= LinearLayoutManager(requireActivity())
        todoViewModel.getAllTodosByTimestampAsc().observe(this, Observer{
            if (it.data != null) {
                rvAdapter.submitList(it.data.map { todoOrException ->
                    // map to just the t.odo.
                    if(todoOrException.data != null) {
                        Log.d(TAG, todoOrException.data.text)
                        todoOrException.data
                    } else {
                        Log.e(TAG, "error with individual Todo", todoOrException.exception)
                        null
                    }.takeUnless{it == null}
                })
            } else {
                Log.e(TAG, "Error with todo list", it.exception)
            }
        })

    }
}


class TodoListAdapter() : ListAdapter<Todo, TodoListAdapter.VH>(GenericDiffCallback()) {
    class VH(val view:View): RecyclerView.ViewHolder(view) {
        fun bind(todo:Todo) {
            view.todo_text.text = todo.text
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_item_todos, parent, false)
        return VH(view)
    }
}
