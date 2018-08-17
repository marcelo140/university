use petgraph::algo::{connected_components, is_cyclic_undirected};
use petgraph::dot::{Config, Dot};
use petgraph::graph::NodeIndex;
use petgraph::{Graph, Undirected};

use rand::{self, Rng};
use std::ops::{Index, IndexMut};

pub enum GraphType {
    Preferential,
    Connected,
}

impl GraphType {
    pub fn build(&self, nodes: usize) -> Graph<usize, (), Undirected> {
        match *self {
            GraphType::Preferential => build_preferential_graph(nodes),
            GraphType::Connected => build_connected_graph(nodes),
        }
    }
}

pub fn build_connected_graph(nodes: usize) -> Graph<usize, (), Undirected> {
    let mut graph = Graph::<usize, (), Undirected>::new_undirected();

    for n in 0..nodes {
        graph.add_node(n);
    }

    let indices: Vec<NodeIndex> = graph.node_indices().collect();
    let mut rnd = rand::thread_rng();

    while connected_components(&graph) > 1 {
        let a = rnd.choose(&indices).unwrap();
        let b = rnd.choose(&indices).unwrap();

        graph.update_edge(*a, *b, ());
    }

    graph
}

fn select_by_degree(graph: &Graph<usize, (), Undirected>, mut selected: i32) -> NodeIndex {
    for node_idx in graph.node_indices() {
        let weight = graph.index(node_idx);
        selected -= *weight as i32;

        if selected <= 0 {
            return node_idx;
        }
    }

    unreachable!();
}

pub fn build_preferential_graph(nodes: usize) -> Graph<usize, (), Undirected> {
    let mut graph = Graph::<_, (), Undirected>::new_undirected();

    for _ in 0..nodes {
        graph.add_node(1);
    }

    let indices: Vec<NodeIndex> = graph.node_indices().collect();
    let mut rnd = rand::thread_rng();
    let mut max = nodes;

    while connected_components(&graph) > 1 {
        let selected: i32 = rnd.gen_range(0, max as i32);
        let a = rnd.choose(&indices).unwrap();
        let b = select_by_degree(&graph, selected);

        let edge = graph.update_edge(*a, b, ());

        if *a == b || is_cyclic_undirected(&graph) {
            graph.remove_edge(edge);
        } else {
            *graph.index_mut(*a) += 1;
            *graph.index_mut(b) += 1;
            max += 2;
        }
    }

    graph
}

pub fn print_graph(graph: &Graph<usize, (), Undirected>) {
    println!("{:?}", Dot::with_config(graph, &[Config::EdgeNoLabel]));
}
