package com.example.vok

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v8._click
import com.github.mvysny.kaributesting.v8._get
import com.github.mvysny.kaributesting.v8._value
import com.github.vokorm.findAll
import com.vaadin.ui.Button
import com.vaadin.ui.TextArea
import com.vaadin.ui.TextField
import kotlin.test.expect

class ArticleEditorTest : DynaTest({
    usingApp()
    beforeEach { login() }

    test("smoke") {
        ArticleEditor()
    }
    test("populate fields") {
        val a = Article(title = "Foo", text = "Bar")
        val editor = ArticleEditor()
        editor.article = a
        expect("Foo") { editor._get<TextField> { caption = "Title" }._value }
        expect("Bar") { editor._get<TextArea>()._value }
    }

    test("save succeeds if article is valid") {
        val editor = ArticleEditor()
        editor.article = Article()
        editor._get<TextField> { caption = "Title" }._value = "My Article"
        editor._get<TextArea>()._value = "The body of the article"
        editor._get<Button> { caption = "Save Article" }._click()
        val articles = Article.findAll()
        expect(1) { articles.size }
        expect("My Article") { articles[0].title }
        expect("The body of the article") { articles[0].text }
    }

    test("go back") {
        val editor = ArticleEditor()
        editor._get<Button> { caption = "Back" } ._click()
        expectView<ArticlesView>()
    }
})