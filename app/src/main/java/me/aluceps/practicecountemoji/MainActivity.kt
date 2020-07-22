package me.aluceps.practicecountemoji

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.aluceps.practicecountemoji.databinding.ActivityMainBinding
import java.text.BreakIterator

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.inputData.observe(this, Observer { text ->
            Log.d("###", "text=$text")
            viewModel.inputDataCount.postValue("%d".format(text.length))
        })

        viewModel.inputDataCount.observe(this, Observer {
            Log.d("###", "count=$it")
        })
    }
}

class MainViewModel : ViewModel() {
    val inputData = MutableLiveData<String>()
    val inputDataCount = MutableLiveData<String>()
}

@BindingAdapter("grapheme_length")
fun TextView.getGraphemeLength(text: String?) {
    if (text == null) {
        0
    } else {
        val instance = BreakIterator.getCharacterInstance()
        instance.setText(text)
        instance.first()
        var counter = 0
        while (instance.next() != BreakIterator.DONE) {
            counter++
        }
        counter
    }.let {
        Log.d("### Grapheme", "count=$it")
        this.text = "%d".format(it)
    }
}