package com.luka.mackovic.eus.usecase.crud;

import com.luka.mackovic.eus.domain.exception.CreateNewsValidationException;
import com.luka.mackovic.eus.domain.model.News;
import com.luka.mackovic.eus.repository.network.NewsRepository;
import com.luka.mackovic.eus.ui.view.CreateNewsFormValueType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Single;

public class NewsCrudUseCase {

    private final NewsRepository newsRepository;

    @Inject
    NewsCrudUseCase(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public Single<List<News>> getNews() {
        return newsRepository.getAll();
    }

    public Single<News> saveNews(News news) {
        Set<CreateNewsFormValueType> validationErrors = this.validateNews(news);

        if (!validationErrors.isEmpty()) {
            return Single.error(new CreateNewsValidationException(validationErrors));
        }
        return newsRepository.save(news);
    }

    public void deleteNews(String newsId) {
        newsRepository.delete(newsId);
    }

    public Single<News> getNews(String newsId) {
        return newsRepository.findById(newsId);
    }

    private boolean checkNewsTitle(String articleTitle) {
        return !articleTitle.isEmpty() && articleTitle.length() < 30;
    }

    private boolean checkNewsContent(String articleContent) {
        return !articleContent.isEmpty();
    }

    private Set<CreateNewsFormValueType> validateNews(News news) {
        Set<CreateNewsFormValueType> validationErrors = new HashSet<>();

        if (!checkNewsTitle(news.getTitle())) {
            validationErrors.add(CreateNewsFormValueType.TITLE);
        }
        if (!checkNewsContent(news.getContent())) {
            validationErrors.add(CreateNewsFormValueType.CONTENT);
        }
        return validationErrors;
    }
}