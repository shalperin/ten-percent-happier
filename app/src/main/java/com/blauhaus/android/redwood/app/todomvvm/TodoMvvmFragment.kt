package com.blauhaus.android.redwood.app.todomvvm

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.blauhaus.android.redwood.app.R
import com.blauhaus.android.redwood.app.login.LoginViewModel
import kotlinx.android.synthetic.main.todomvvm_fragment_main.*
import kotlinx.android.synthetic.main.todomvvm_rv_item_todos_list.view.*
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
        return inflater.inflate(R.layout.todomvvm_fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var rvAdapter = TodoListAdapter(todoViewModel, this )
        todo_rv.let {
            it.adapter = rvAdapter
            it.layoutManager = LinearLayoutManager(requireActivity())
        }

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
                } })

        todoViewModel.getAllTodosByTimestampAsc().observe(this, Observer{
            if (it.data == null) {
                handleException("Error with todos by timestamp query", it.exception)
            } else {
                val todos: List<Todo> = it.data.map { todoOrException ->
                    if( todoOrException.data == null) {
                        handleException("error with individual Todo", todoOrException.exception)
                        null
                    } else {
                        todoOrException.data
                    }
                }.filterNotNull()

                rvAdapter.submitList(todos)
            }
        })

        todo_input.setOnEditorActionListener( object: TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    todoViewModel.addTodo(todo_input.text.toString())
                    todo_input.setText("")
                }
                return false
            }
        })
    }

    //Todo: crashylitcs
    fun handleException(msg:String, e: Exception?) {
        Log.e(TAG, msg, e)
        Toast.makeText(requireActivity(), R.string.wrong, Toast.LENGTH_LONG ).show()
    }
}



class TodoListAdapter(val viewModel:TodoViewModel, val lifecycleOwner: LifecycleOwner) : ListAdapter<Todo, TodoListAdapter.VH>(GenericDiffCallback()) {
    val TAG = "TODO_LIST_ADAPTER"

    inner class VH(val view:View): RecyclerView.ViewHolder(view) {
        fun bind(todo:Todo) {
            view.todo_text.text = todo.text
            if (todo.complete) {
                view.checked_icon.visibility = View.VISIBLE
            } else {
                view.checked_icon.visibility = View.GONE
            }
            //TODO is this a source of jank?  Should we be binding the listener to the viewholder or sumptin?
            view.unchecked_icon.setOnClickListener {
                viewModel.toggleTodoComplete(todo.id, !todo.complete)
            }
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.todomvvm_rv_item_todos_list, parent, false)
        return VH(view)
    }
}
