package com.darooma.radmoviesdbp1.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.darooma.radmoviesdbp1.application.RadMoviesDBApp
import com.darooma.radmoviesdbp1.data.RadMoviesRepository
import com.darooma.radmoviesdbp1.data.db.model.RadMoviesEntity
import com.darooma.radmoviesdbp1.R
import com.darooma.radmoviesdbp1.databinding.MovieDialogBinding
import kotlinx.coroutines.launch
import java.io.IOException

class RadMovieDialog(
    private val newRadMovie: Boolean = true,
    private val radMovie: RadMoviesEntity = RadMoviesEntity(
        title = "",
        producer = "",
        director = "",
        genre = "",
        rating = ""
    ), private val updateUI: () -> Unit,
    private val message: (String) -> Unit
) : DialogFragment() {

    private var _binding: MovieDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private var saveButton: Button? = null

    private lateinit var repository: RadMoviesRepository

    //Se configura el diálogo inicial
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = MovieDialogBinding.inflate(requireActivity().layoutInflater)

        repository = (requireContext().applicationContext as RadMoviesDBApp).repository

        builder = AlertDialog.Builder(requireContext())

//Aqui se le agrega el selector al spinner


        /*
        binding.tietTitle.setText(game.title)
        binding.tietGenre.setText(game.genre)
        binding.tietDeveloper.setText(game.developer)
        */



        binding.apply {
            tietTitle.setText(radMovie.title)
            tietProducer.setText(radMovie.producer)
            tietDirector.setText(radMovie.director)

        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.genres_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.spGenre.adapter = adapter
            binding.spGenre.setSelection(adapter.getPosition(radMovie.genre))
            saveButton?.isEnabled = false
        }



        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.ratings_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.spRating.adapter = adapter
            binding.spRating.setSelection(adapter.getPosition(radMovie.rating))
            saveButton?.isEnabled = false
        }

        dialog = if(newRadMovie){
            buildDialog(getString(R.string.btn_save), getString(R.string.btn_cancel), {
                 //Create (guardar)
                radMovie.title = binding.tietTitle.text.toString()
                radMovie.producer = binding.tietProducer.text.toString()
                radMovie.director = binding.tietDirector.text.toString()
                radMovie.genre = binding.spGenre.selectedItem.toString()
                radMovie.rating = binding.spRating.selectedItem.toString()

                try{
                    lifecycleScope.launch {
                        repository.insertRadMovie(radMovie)
                    }
                    message(getString(R.string.msg_saved_movie))
                    //Actualizar UI
                    updateUI()
                }catch(e: IOException){
                    message(getString(R.string.msg_error_saving))
                }
            },{
                //cancelar
            })
        }else{
            buildDialog(getString(R.string.btn_update), getString(R.string.btn_delete), {
                //Update
                radMovie.title = binding.tietTitle.text.toString()
                radMovie.producer = binding.tietProducer.text.toString()
                radMovie.director = binding.tietDirector.text.toString()
                radMovie.genre = binding.spGenre.selectedItem.toString()
                radMovie.rating = binding.spRating.selectedItem.toString()
                try{
                    lifecycleScope.launch {
                        repository.updateRadMovie(radMovie)
                    }
                    message(getString(R.string.msg_updated))
                    //Actualizar UI
                    updateUI()
                }catch(e: IOException){
                    message(getString(R.string.msg_update_error))
                }
            }, {
                //Delete
                //Delete

                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.msg_confirm))
                    .setMessage("¿${getString(R.string.msg_confirm_to_delete)} ${radMovie.title}?")
                    .setPositiveButton(getString(R.string.btn_acept)){ _, _ ->
                        try {
                            lifecycleScope.launch {
                                repository.deleteRadMovie(radMovie)
                            }

                            message(getString(R.string.msg_movie_deleted))

                            //Actualizar la UI
                            updateUI()

                        }catch(e: IOException){
                            e.printStackTrace()
                            message(getString(R.string.msg_error_deleting))
                        }
                    }
                    .setNegativeButton(getString(R.string.btn_cancel)){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()

            })
        }
        return dialog
    }

    //Cuando se destruye el fragment
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //Se llama después de que se muestra el diálogo en pantalla
    override fun onStart() {
        super.onStart()

        val alertDialog = dialog as AlertDialog //Lo usamos para poder emplear el método getButton (no lo tiene el dialog)
        saveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false

        binding.tietTitle.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        })

        binding.tietProducer.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

        binding.tietDirector.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

        binding.spGenre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Log.d("REVCHANGE", "Tenemos  posicion ${position} y id ${id}")
                saveButton?.isEnabled = validateFields()
            }

        }

        binding.spRating.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Log.d("REVCHANGE", "Tenemos  posicion ${position} y id ${id}")
                saveButton?.isEnabled = validateFields()
            }

        }
    }

    private fun validateFields() =
        (binding.tietTitle.text.toString().isNotEmpty() && binding.tietProducer.text.toString()
            .isNotEmpty() && binding.tietDirector.text.toString()
            .isNotEmpty() && binding.spGenre.selectedItemPosition != 0 && binding.spRating.selectedItemPosition != 0) //Agregar spiners

    private fun buildDialog(btn1Text: String, btn2Text: String, positiveButton: () ->Unit, negativeButton: () -> Unit): Dialog =
        builder.setView(binding.root)
            .setTitle(getString(R.string.gnrl_movie))
            .setPositiveButton(btn1Text, DialogInterface.OnClickListener { dialog, which ->
                //Acción para el botón positivo
                positiveButton()
            })
            .setNegativeButton(btn2Text){ _, _ ->
                //Acción para el botón negativo
                negativeButton()
            }
            .create()
}

