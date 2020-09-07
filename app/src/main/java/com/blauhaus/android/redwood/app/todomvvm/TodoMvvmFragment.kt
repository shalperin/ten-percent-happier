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
import androidx.lifecycle.*
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

class TodoMvvmFragment() : Fragment() {
    private val loginViewModel by viewModel<LoginViewModel>()
    private val todoViewModel by viewModel<TodoViewModel>()

    private val TAG = TodoMvvmFragment::class.java.simpleName

    var rvAdapter:TodoListAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.todomvvm_fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.authenticationState.observe(viewLifecycleOwner, Observer {
            todoViewModel.setAuthState(it)
            if (it == LoginViewModel.AuthenticationState.UNAUTHENTICATED) {
                findNavController().navigate(R.id.action_global_loginFragment)
            }
        })

        rvAdapter = TodoListAdapter(todoViewModel, this )
        todo_rv.let {
            it.adapter = rvAdapter
            it.layoutManager = LinearLayoutManager(requireActivity())
        }

        bindObservers()
        addListeners()
    }

    //Todo: crashylitcs
    fun handleException(msg:String, e: Exception?) {
        Log.e(TAG, msg, e)
        Toast.makeText(requireActivity(), R.string.wrong, Toast.LENGTH_LONG ).show()
    }

    fun bindObservers() {
       todoViewModel._filteredTodos.observe(viewLifecycleOwner, Observer{
            var todos = it.map { todoOrException ->
                    if( todoOrException.data == null) {
                        handleException("error with individual Todo", todoOrException.exception)
                        null
                    } else {
                        todoOrException.data
                    }
                }.filterNotNull()

                rvAdapter?.submitList(todos)
            }
        )

        todoViewModel.filterMode.observe (viewLifecycleOwner, Observer {
            when (it) {
                TodoViewModel.TodoFilterMode.ACTIVE -> {
                    all_btn.setBackgroundResource(0)
                    completed_btn.setBackgroundResource(0)
                    active_btn.setBackgroundResource(R.drawable.todo_active_filter_background)
                }
                TodoViewModel.TodoFilterMode.COMPLETED -> {
                    all_btn.setBackgroundResource(0)
                    completed_btn.setBackgroundResource(R.drawable.todo_active_filter_background)
                    active_btn.setBackgroundResource(0)

                }
                TodoViewModel.TodoFilterMode.ALL -> {
                    all_btn.setBackgroundResource(R.drawable.todo_active_filter_background)
                    completed_btn.setBackgroundResource(0)
                    active_btn.setBackgroundResource(0)

                }
            }
        })

        todoViewModel.allTodosAreComplete().observe(viewLifecycleOwner, Observer {
            if (it) {
                toggle_all_completed_btn.setImageDrawable(resources.getDrawable(R.drawable.todo_ic_keyboard_arrow_down_active))  //TODO FIXME
            } else {
                toggle_all_completed_btn.setImageDrawable(resources.getDrawable(R.drawable.todo_ic_keyboard_arrow_down))
            }
        })

        todoViewModel.incompleteCount().observe(viewLifecycleOwner, Observer {
            todo_count.text = getString(R.string.items_left, it)
        })

    }

    fun addListeners() {
        todo_input.setOnEditorActionListener( object: TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    todoViewModel.addTodo(todo_input.text.toString())
                    todo_input.setText("")
                }
                return false
            }
        })

        toggle_all_completed_btn.setOnClickListener{
            var value = todoViewModel.allTodosAreComplete().value
            if (value == null || value == false) {
                todoViewModel.markAllTodosCompletion(true)
            } else {
                todoViewModel.markAllTodosCompletion(false)
            }
        }

        all_btn.setOnClickListener {
            todoViewModel.setFilterMode(TodoViewModel.TodoFilterMode.ALL)
        }

        active_btn.setOnClickListener {
            todoViewModel.setFilterMode(TodoViewModel.TodoFilterMode.ACTIVE)
        }

        completed_btn.setOnClickListener {
            todoViewModel.setFilterMode(TodoViewModel.TodoFilterMode.COMPLETED)
        }

    }
}


class TodoListAdapter(val viewModel:TodoViewModel, val lifecycleOwner: LifecycleOwner) : ListAdapter<Todo, TodoListAdapter.VH>(GenericDiffCallback()) {
    val TAG = "TODO_LIST_ADAPTER"

    inner class VH(val view:View): RecyclerView.ViewHolder(view) {
        fun bind(todo:Todo) {
            view.todo_text.text = todo.text
            view.todo_edit_text.setText(todo.text)
            showCheckmark(view, todo.complete)
            //TODO is this a source of jank?  Should we be binding the listener to the viewholder or sumptin?
            view.unchecked_icon.setOnClickListener {
                showCheckmark(view, !todo.complete)
                viewModel.toggleTodoComplete(todo.id, !todo.complete)
            }
            view.delete_btn.setOnClickListener{
                viewModel.deleteTodo(todo.id)
            }
            view.todo_text_container.setOnLongClickListener{
                view.todo_edit_text.visibility = View.VISIBLE
                view.todo_text.visibility = View.GONE
                true
            }
            view.todo_edit_text.setOnEditorActionListener( object: TextView.OnEditorActionListener {
                override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        viewModel.updateTodo(todo.id, view.todo_edit_text.text.toString())
                        view.todo_text.text = view.todo_edit_text.text
                        view.todo_text.visibility = View.VISIBLE
                        view.todo_edit_text.visibility = View.GONE
                    }
                    return false
                }
            })
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

    fun showCheckmark(view: View, showComplete:Boolean) {
        if (showComplete) {
            view.checked_icon.visibility = View.VISIBLE
        } else {
            view.checked_icon.visibility = View.GONE
        }
    }
}
